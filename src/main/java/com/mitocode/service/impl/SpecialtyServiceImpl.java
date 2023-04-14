package com.mitocode.service.impl;

import com.mitocode.model.Specialty;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.ISpecialtyRepo;
import com.mitocode.service.ISpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl extends CRUDImpl<Specialty, Integer> implements ISpecialtyService {

    //@Autowired
    private final ISpecialtyRepo repo;// = new SpecialtyRepo();

    @Override
    protected IGenericRepo<Specialty, Integer> getRepo() {
        return repo;
    }



    /*@Override
    public Specialty save(Specialty patient) {
        return repo.save(patient);
    }

    @Override
    public Specialty update(Specialty patient) {
        return repo.save(patient);
    }

    @Override
    public List<Specialty> findAll() {
        return repo.findAll();
    }

    @Override
    public Specialty findById(Integer id) {
        //Optional<Specialty> op = repo.findById(id);
        //return op.isPresent() ? op.get() : new Specialty();

        return repo.findById(id).orElse(new Specialty());
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
    */


    /*public SpecialtyService(SpecialtyRepo repo){
        this.repo = repo;
    }*/

    /*public String sayHello(Specialty p){
        return repo.sayHello(p);
    }*/
}
