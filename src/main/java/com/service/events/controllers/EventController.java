package com.service.events.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.service.events.model.Event;
import com.service.events.model.EventStatus;
import com.service.events.model.HttpTask;
import com.service.events.model.LoginForm;
import com.service.events.services.CredentialRepository;
import com.service.events.services.EventsRepository;
import com.service.events.services.HttpProcessor;
import com.service.events.services.HttpResponseListener;

@Controller
public class EventController {

	@Autowired
	CredentialRepository credentialRepository;

	@Autowired
	EventsRepository eventsRepository;

	@Resource(name = "httpProcessor")
	private HttpProcessor httpProcessor;

	@Value("${firebase.post.url}")
	private String firebaseUrl;

	@RequestMapping("/")
	public String welcome(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			// errors processing
			System.out.println("Errors");
		}

		System.out.println("o:" + loginForm);

		// credentialRepository.save(loginForm);

		LoginForm form = credentialRepository.findByUsername(loginForm.getUsername());
		if (form.getUsername().equalsIgnoreCase(loginForm.getUsername())
				&& form.getPassword().equals(loginForm.getPassword())) {

			model.addAttribute("event", new Event());
			model.addAttribute("events", eventsRepository.findAll());
			return "welcome";
		}

		return "authfailed";
	}

	@PostMapping("/events")
	public String addEvent(@ModelAttribute Event event, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			// errors processing
			System.out.println("Errors");
		}

		try {
			
			System.out.println("Event :"+event);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			long delay = sdf.parse(event.getScheduleTime()).getTime() - (new Date()).getTime();
			if (delay < 0)
				throw new Exception("Past date");
			
			System.out.println("::"+delay);
			
			event.setStatus(EventStatus.SCHEDULED);
			Event updatedEvent = eventsRepository.save(event);
			HttpTask command = new HttpTask(firebaseUrl, updatedEvent);
			command.setResponseListener(new HttpResponseListener(eventsRepository));
			httpProcessor.scheduleTask(command, delay, TimeUnit.MILLISECONDS);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error :" + e.getMessage());
			model.addAttribute("hasError", true);
			model.addAttribute("error", "Event Name exists !!");
			return "welcome";

		}

		model.addAttribute("events", eventsRepository.findAll());
		return "welcome";

	}

	@RequestMapping(value = { "/events/{id}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.DELETE })
	public String deleteEvent(@PathVariable String id, Model model) {

		try {
			eventsRepository.deleteById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.addAttribute("hasError", true);
			model.addAttribute("error", "Event Name exists !!");
			return "welcome";
		}

		model.addAttribute("events", eventsRepository.findAll());
		return "welcome";
	}

	@RequestMapping(value = { "/events" }, method = {RequestMethod.PUT })
	public String updateEvent(@ModelAttribute Event event, BindingResult bindingResult, Model model) {

		try {
			eventsRepository.deleteById(event.getId());
			eventsRepository.save(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.addAttribute("hasError", true);
			model.addAttribute("error", "Event update error !!");
			return "welcome";
		}

		model.addAttribute("events", eventsRepository.findAll());
		return "welcome";
	}

}
