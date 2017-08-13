package com.service.events.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.service.events.common.Constant;
import com.service.events.model.Event;
import com.service.events.model.EventStatus;
import com.service.events.model.HttpTask;
import com.service.events.model.LoginForm;
import com.service.events.services.CredentialRepository;
import com.service.events.services.EventService;
import com.service.events.services.HttpProcessor;
import com.service.events.services.HttpResponseListener;

@Controller
public class EventController {

	@Autowired
	CredentialRepository credentialRepository;


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
			model.addAttribute("event", new Event());
			model.addAttribute("events", eventService.findAll());
			return "welcome";
	
	}

	@Secured ({"ROLE_ADMIN"})
	@PostMapping("/admin/events")
	public String addEvent(@ModelAttribute Event event, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			// errors processing
			System.out.println("Errors");
		}

		try {
			
			System.out.println("Event :"+event);
			SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);

			long delay = sdf.parse(event.getScheduleTime()).getTime() - (new Date()).getTime();			
			Event updatedEvent = eventService.save(event);
			
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
			return "welcome";

		}

		model.addAttribute("events", eventService.findAll());
		return "welcome";

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
			return "welcome";
		}

		model.addAttribute("events", eventService.findAll());
		return "welcome";
	}

	
	
	@Secured ({"ROLE_ADMIN"})
	@PutMapping("/admin/events")
	public String updateEvent(@ModelAttribute Event event, BindingResult bindingResult, Model model) {

		try {
			eventService.deleteById(event.getId());
			eventService.save(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.addAttribute("hasError", true);
			model.addAttribute("error", "Event update error !!");
			return "welcome";
		}

		model.addAttribute("events", eventService.findAll());
		return "welcome";
	}

	
	 @PostMapping("/admin/file")
	    public String handleFileUpload(@RequestParam("file") MultipartFile file,
	            RedirectAttributes redirectAttributes) {

	        storageService.store(file);
	        redirectAttributes.addFlashAttribute("message",
	                "You successfully uploaded " + file.getOriginalFilename() + "!");

	        return "redirect:/";
	    }
	 
}
