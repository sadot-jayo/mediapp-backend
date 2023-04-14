package com.mitocode.controller;

import com.mitocode.dto.ConsultDTO;
import com.mitocode.dto.ConsultListExamDTO;
import com.mitocode.dto.ConsultProcDTO;
import com.mitocode.dto.IConsultProcDTO;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.model.FilterConsultDTO;
import com.mitocode.model.MediaFile;
import com.mitocode.service.IMediaFileService;
import com.mitocode.service.impl.ConsultServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/consults")
@RequiredArgsConstructor
public class ConsultController {

    //@Autowired
    private final ConsultServiceImpl service;// = new ConsultService();

    private final IMediaFileService mfService;

    @Qualifier("consultMapper")
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ConsultDTO>> findAll() {
        List<ConsultDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id) {
        Consult obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ConsultListExamDTO dto) {
        Consult cons = this.convertToEntity(dto.getConsult());
        List<Exam> exams = mapper.map(dto.getLstExam(), new TypeToken<List<Exam>>() {}.getType());

        Consult obj = service.saveTransactional(cons, exams);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult()).toUri();
        return ResponseEntity.created(location).build();
    }

    /*@PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ConsultDTO dto){
        Consult obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult()).toUri();
        return ResponseEntity.created(location).build();
    }*/

    @PutMapping("/{id}")//(consumes = "", produces = "")
    public ResponseEntity<ConsultDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody ConsultDTO dto) {
        dto.setIdConsult(id);
        Consult obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    //@ResponseStatus(HttpStatus.NOT_FOUND)
    /*@GetMapping("/hateoas/{id}")
    public EntityModel<ConsultDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<ConsultDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(LanguageController.class).changeLocale("EN"));
        resource.add(link1.withRel("specialty-info1"));
        resource.add(link2.withRel("language-info"));

        return resource;
    }*/

    @PostMapping("/search/others")
    public ResponseEntity<List<ConsultDTO>> searchByOthers(@RequestBody FilterConsultDTO filterDTO) {
        List<Consult> consults = service.search(filterDTO.getDni(), filterDTO.getFullname());
        List<ConsultDTO> consultsDTO = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());

        return new ResponseEntity<>(consultsDTO, HttpStatus.OK);
    }

    @GetMapping("/search/date")
    public ResponseEntity<List<ConsultDTO>> searchByDates(@RequestParam(value = "date1", defaultValue = "2023-02-25", required = true) String date1, @RequestParam(value = "date2") String date2) {
        List<Consult> consults = service.searchByDates(LocalDateTime.parse(date1), LocalDateTime.parse(date2));
        List<ConsultDTO> consultsDTO = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());

        return new ResponseEntity<>(consultsDTO, HttpStatus.OK);
    }

    /*@GetMapping("/callProcedure")
    public ResponseEntity<List<ConsultProcDTO>> callProcedureOrFunction(){
        List<ConsultProcDTO> consults = service.callProcedureOrFunction();
        return new ResponseEntity<>(consults, OK);
    }*/

    @GetMapping("/callProcedure")
    public ResponseEntity<List<IConsultProcDTO>> callProcedureOrFunction(){
        List<IConsultProcDTO> consults = service.callProcedureOrFunction();
        return new ResponseEntity<>(consults, OK);
    }

    @GetMapping("/callProcedure2")
    public ResponseEntity<List<IConsultProcDTO>> callProcedureOrFunction2(){
        List<IConsultProcDTO> consults = service.callProcedureorFunction2();
        return new ResponseEntity<>(consults, OK);
    }

    @GetMapping(value = "/generateReport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE) //MediaType.APPLICATION_PDF_VALUE
    public ResponseEntity<byte[]> generateReport() throws Exception{
        byte[] data = service.generateReport();
        return new ResponseEntity<>(data, OK);
    }

    @PostMapping(value = "/saveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveFile(@RequestParam("file")MultipartFile file) throws Exception{
        MediaFile mf = new MediaFile();
        mf.setFiletype(file.getContentType());
        mf.setFilename(file.getOriginalFilename());
        mf.setValue(file.getBytes());

        mfService.save(mf);

        return new ResponseEntity<>(OK);
    }


    @GetMapping(value = "/readFile/{idFile}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> readFile(@PathVariable("idFile") Integer idFile) throws IOException {

        byte[] arr = mfService.findById(idFile).getValue();

        return new ResponseEntity<>(arr, HttpStatus.OK);
    }

    private ConsultDTO convertToDto(Consult obj) {
        return mapper.map(obj, ConsultDTO.class);
    }

    private Consult convertToEntity(ConsultDTO dto) {
        return mapper.map(dto, Consult.class);
    }

}
