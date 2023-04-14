package com.mitocode.controller;

import com.mitocode.dto.ExamDTO;
import com.mitocode.model.Exam;
import com.mitocode.service.impl.ExamServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    //@Autowired
    private final ExamServiceImpl service;// = new ExamService();

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExamDTO>> findAll(){
        List<ExamDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id){
        Exam obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ExamDTO dto){
        Exam obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExam()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")//(consumes = "", produces = "")
    public ResponseEntity<ExamDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody ExamDTO dto){
        dto.setIdExam(id);
        Exam obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    //@ResponseStatus(HttpStatus.NOT_FOUND)
    /*@GetMapping("/hateoas/{id}")
    public EntityModel<ExamDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<ExamDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLocale("EN"));
        resource.add(link1.withRel("exam-info1"));
        resource.add(link2.withRel("language-info"));

        return resource;
    }*/

    private ExamDTO convertToDto(Exam obj){
        return mapper.map(obj, ExamDTO.class);
    }

    private Exam convertToEntity(ExamDTO dto){
        return mapper.map(dto, Exam.class);
    }

}
