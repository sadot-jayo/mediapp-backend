package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.exception.NewModelNotFoundException;
import com.mitocode.model.Patient;
import com.mitocode.service.impl.PatientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    //@Autowired
    private final PatientServiceImpl service;// = new PatientService();

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll(){
        List<PatientDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    /*
    @GetMapping
    public ResponseEntity<List<PatientRecord>> findAll(){
        List<PatientRecord> list = service.findAll().stream().map(e -> {
            return new PatientRecord(e.getIdPatient(), e.getFirstName());
        }).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }
     */

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id){
        Patient obj = service.findById(id);

        /*if(obj == null){
            throw new ModelNotFoundException("ID NOT FOUND" + id);
            //throw new NewModelNotFoundException("ID NOT FOUND " + id);
        }*/

        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    /*@PostMapping
    public ResponseEntity<Patient> save(@RequestBody Patient patient){
        Patient obj = service.save(patient);
        return new ResponseEntity<>(obj, CREATED);
    }*/

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody PatientDTO dto){
        Patient obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPatient()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")//(consumes = "", produces = "")
    public ResponseEntity<PatientDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody PatientDTO dto){
        dto.setIdPatient(id);
        Patient obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        //Patient obj = service.findById(id);

        /*if(obj == null){
            //throw new ModelNotFoundException("ID NOT FOUND " + id);
            throw new NewModelNotFoundException("ID NOT FOUND " + id);
        }*/
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    //@ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping("/hateoas/{id}")
    public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<PatientDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLocale("EN"));
        resource.add(link1.withRel("patient-info1"));
        resource.add(link2.withRel("language-info"));

        return resource;
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<PatientDTO>> listPage(Pageable pageable){
        Page<PatientDTO> page = service.listPage(pageable).map(p -> mapper.map(p, PatientDTO.class));
        return new ResponseEntity<>(page, OK);
    }

    private PatientDTO convertToDto(Patient obj){
        return mapper.map(obj, PatientDTO.class);
    }

    private Patient convertToEntity(PatientDTO dto){
        return mapper.map(dto, Patient.class);
    }

    /*@GetMapping
    public String sayHello(){
        Patient p = new Patient();
        p.setIdPatient(1);
        p.setName("Mito");
        return service.sayHello(p);
    }*/

}
