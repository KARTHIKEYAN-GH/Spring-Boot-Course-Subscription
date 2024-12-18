package com.project.course.subscription.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.course.subscription.dto.PaxHeadDTO;
import com.project.course.subscription.dto.PaxMemberPostDTO;
import com.project.course.subscription.dto.PaxUsersDTO;
import com.project.course.subscription.model.PaxUser;
import com.project.course.subscription.service.PaxUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admin/pax")
public class PaxUserController {

	@Autowired
	private PaxUserService paxUserService;

	@PostMapping("/addHead")
	public ResponseEntity<Object> addPaxHead(@Valid @RequestBody PaxUser paxUser) {
		try {
			PaxUser createdUser = paxUserService.addPaxHead(paxUser);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/updateHead/{uuid}")
	public ResponseEntity<?> updateHead(@PathVariable String uuid, @RequestBody PaxUser request) {
		try {
			PaxUser updatedPaxHead = paxUserService.updatePaxHead(uuid, request);
			return new ResponseEntity<>(updatedPaxHead, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PutMapping("/UpdateMember/{uuid}")
	public ResponseEntity<?> updateMember(@PathVariable String uuid, @RequestBody PaxUser request) {
		try {
			PaxUser updatedPaxMember = paxUserService.updatePaxMember(uuid, request);
			return new ResponseEntity<>(updatedPaxMember, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@DeleteMapping("/dropPaxUser/{uuid}")
	public ResponseEntity<?> dropPaxUser(@PathVariable String uuid) {
		try {
			PaxUser dropPaxHead = paxUserService.dropPaxUser(uuid);
			return new ResponseEntity<>(dropPaxHead.getName() + " Deleted Successfully", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<?> getPaxHeadById(@PathVariable String uuid) {
		try {
			PaxHeadDTO paxHead = paxUserService.getPaxHeadById(uuid);
			return new ResponseEntity<>(paxHead, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/getMember/{uuid}")
	public ResponseEntity<?> getPaxMemberById(@PathVariable String uuid) {
		try {
			PaxUsersDTO paxHead = paxUserService.getPaxMemberById(uuid);
			return new ResponseEntity<>(paxHead, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/addPaxMembers/{uuid}")
	public ResponseEntity<?> addPaxMembers(@PathVariable String uuid, @RequestBody PaxMemberPostDTO paxMember) {
		try {
			PaxUser addPaxMember = paxUserService.addPaxMembers(uuid, paxMember);
			return ResponseEntity.status(HttpStatus.CREATED).body(addPaxMember);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/getMemberByHeadId/{uuid}")
	public ResponseEntity<?> getPaginatedAndSortedMembers(@PathVariable String uuid,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<PaxUsersDTO> AllMember = paxUserService.getAllPaginatedAndSortedPaxMembersByHeadUuid(uuid, page, size, sortBy, direction);
			return new ResponseEntity<>(AllMember, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/head")
	public Page<PaxHeadDTO> getPaginatedAndSortedHeads(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		return paxUserService.getAllPaginatedAndSortedHeads(page, size, sortBy, direction);
	}

	@GetMapping("/searchHead")
	public ResponseEntity<List<PaxHeadDTO>> searchByHeadItems(@RequestParam String query,@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		List<PaxHeadDTO> results = paxUserService.searchHead(query,sortBy, direction);
		return ResponseEntity.ok(results);
	}

	@GetMapping("/searchMember/{uuid}")
	public ResponseEntity<List<PaxUsersDTO>> searchByMemberItems(@PathVariable String uuid,
			@RequestParam String query,@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		List<PaxUsersDTO> results = paxUserService.searchMemberByHeadUuid(uuid, query,sortBy, direction);
		return ResponseEntity.ok(results);
	}
}