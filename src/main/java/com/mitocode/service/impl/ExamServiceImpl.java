package com.mitocode.service.impl;

import com.mitocode.model.Exam;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IExamRepo;
import com.mitocode.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends CRUDImpl<Exam, Integer> implements IExamService {

    //@Autowired
    private final IExamRepo repo;// = new ExamRepo();

    @Override
    protected IGenericRepo<Exam, Integer> getRepo() {
        return repo;
    }



    /*@Override
    public Exam save(Exam patient) {
        return repo.save(patient);
    }

    @Override
    public Exam update(Exam patient) {
        return repo.save(patient);
    }

    @Override
    public List<Exam> findAll() {
        return repo.findAll();
    }

    @Override
    public Exam findById(Integer id) {
        //Optional<Exam> op = repo.findById(id);
        //return op.isPresent() ? op.get() : new Exam();

        return repo.findById(id).orElse(new Exam());
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
    */


    /*public ExamService(ExamRepo repo){
        this.repo = repo;
    }*/

    /*public String sayHello(Exam p){
        return repo.sayHello(p);
    }*/
}
