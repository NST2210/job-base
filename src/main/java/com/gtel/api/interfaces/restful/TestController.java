package com.gtel.api.interfaces.restful;

import com.gtel.api.domains.models.postgres.Test;
import com.gtel.api.infrastracture.repository.postgres.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/test")
@RequiredArgsConstructor
public class TestController {
    private final TestRepository testRepository;

    @GetMapping("by-name/{name}")
    public ResponseEntity<Test> getByName(@PathVariable String name) {
        Test test = testRepository.findByName(name);
        return ResponseEntity.ok(test);
    }

    @PostMapping("")
    public Test getByName(@RequestBody Test test) {
        return this.testRepository.save(test);
    }
}
