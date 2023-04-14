package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.dto.SignDTO;
import com.mitocode.model.Sign;
import com.mitocode.service.impl.SignServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/signs")
@RequiredArgsConstructor
public class SignController {

    private final SignServiceImpl service;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<SignDTO>> findAll(){
        List<SignDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignDTO> findById(@PathVariable("id") Integer id){
        Sign obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody SignDTO dto){
        Sign obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSign()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SignDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody SignDTO dto){
        dto.setIdSign(id);
        Sign obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<SignDTO>> listPage(Pageable pageable){
        Page<SignDTO> page = service.listPage(pageable).map(p -> mapper.map(p, SignDTO.class));
        return new ResponseEntity<>(page, OK);
    }

    private SignDTO convertToDto(Sign obj){
        return mapper.map(obj, SignDTO.class);
    }

    private Sign convertToEntity(SignDTO dto){
        return mapper.map(dto, Sign.class);
    }

}
