package com.velikiyprikalel.millFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.velikiyprikalel.millFactory.bean.MillState;

@RestController
@RequestMapping("/rest/mill")
public class MillController {

	private final MillService millService;

	public MillController(MillService millService) {
		this.millService = millService;
	}

	@GetMapping
	public MillState getState() {
		return this.millService.getState();
	}

	@PostMapping("water/{capacity}")
	public String addWater(@PathVariable Integer capacity) {
		this.millService.addWater(capacity);
		return "OK";
	}

	@PostMapping("millet/{capacity}")
	public String addMillet(@PathVariable Integer capacity) {
		this.millService.addMillet(capacity);
		return "OK";
	}

	@PostMapping("flour/drop")
	public String dropFlour() {
		this.millService.dropFlour();
		return "OK";
	}
}
