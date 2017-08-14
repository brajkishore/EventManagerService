package com.nroutes.events.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nroutes.events.common.Constant;
import com.nroutes.events.model.Event;
import com.nroutes.events.model.EventStatus;
import com.nroutes.events.model.HttpTask;
import com.nroutes.events.services.CredentialRepository;
import com.nroutes.events.services.EventService;
import com.nroutes.events.services.HttpProcessor;
import com.nroutes.events.services.HttpResponseListener;
import com.nroutes.events.services.StorageService;

@Controller
public class EventController {

	@Autowired
	CredentialRepository credentialRepository;

	@Autowired
	StorageService storageService;


	@Resource(name = "httpProcessor")
	private HttpProcessor httpProcessor;

	
	@Resource(name = "eventService")
	private EventService eventService;

	
	@Value("${firebase.post.url}")
	private String firebaseUrl;

	@RequestMapping("/")
	public String home() {		
		return "login";
	}
	
	
	
	@RequestMapping("/login")
	public String login() {		
		return "login";
	}
		
	
	@Secured ({"ROLE_ADMIN"})
	@RequestMapping("/admin/event")
	public String event(Model model) {
		model.addAttribute("event", new Event());
		return "event";
	}
	
	@Secured ({"ROLE_ADMIN"})
	@RequestMapping("/admin/events/{id}")
	public String eventEdit(@PathVariable("id") String id,Model model) {		
		Event event=eventService.getById(id);
		model.addAttribute("event", event);
		return "eventEdit";
	}
	
		
		
	@Secured ({"ROLE_ADMIN"})
	@GetMapping("/admin/welcome")
	public String welcome(Model model) {		
			model.addAttribute("events", eventService.findAll());
			return "welcome";
	
	}

	@Secured ({"ROLE_ADMIN"})
	@GetMapping("/admin/events")
	public String showEvents(Model model) {						
			return "redirect:welcome";
	
	}
	
	@Secured ({"ROLE_ADMIN"})
	@PostMapping("/admin/events")
	public String addEvent(@ModelAttribute Event event, BindingResult bindingResult, Model model,
			final @RequestParam(name="imgFile",required = false) MultipartFile imgFile,
			final @RequestParam(name="bgImgFile",required = false) MultipartFile bgImgFile) {

		if (bindingResult.hasErrors()) {
			// errors processing
			System.out.println("Errors");
		}
		

		
		try {
			
			System.out.println("Event :"+event);
			SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);
			
			if(StringUtils.isEmpty(event.getImgUrl()) && imgFile!=null){				
				event.setImgUrl(storageService.store(imgFile));
			}
			
			if(StringUtils.isEmpty(event.getBgImgUrl()) && bgImgFile!=null){				
				event.setBgImgUrl(storageService.store(bgImgFile));
			}			
			
			long delay = sdf.parse(event.getScheduleTime()).getTime() - (new Date()).getTime();			
			Event updatedEvent = eventService.save(event);
			
			System.out.println("ImgFileName:"+imgFile.getName()+"::"+bgImgFile.getOriginalFilename());
						
			
			if (delay >0){				
				event.setStatus(EventStatus.SCHEDULED);				
				HttpTask command = new HttpTask(firebaseUrl, updatedEvent);
				command.setResponseListener(new HttpResponseListener(eventService.getEventsRepository()));
				httpProcessor.scheduleTask(command, delay, TimeUnit.MILLISECONDS);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error :" + e.getMessage());
			model.addAttribute("hasError", true);
			model.addAttribute("error", "Event Name exists !!");			
		}

		model.addAttribute("events", eventService.findAll());
		return "redirect:welcome";

	}

	
	@Secured ({"ROLE_ADMIN"})
	@DeleteMapping("/admin/events/{id}")
	public String deleteEvent(@PathVariable String id, Model model) {
		
		System.out.println("deleting:"+id);
		
		try {
			eventService.deleteById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.addAttribute("hasError", true);
			model.addAttribute("error", "No such an event.");			
		}

		model.addAttribute("events", eventService.findAll());
		return "redirect:welcome";
	}
	
		 
}
