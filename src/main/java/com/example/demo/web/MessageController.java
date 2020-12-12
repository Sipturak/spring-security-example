package com.example.demo.web;

import com.example.demo.model.Message;
import com.example.demo.repository.JpaMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/message")
public class MessageController {

    private final JpaMessageRepository jpaMessageRepository;

    public MessageController(JpaMessageRepository jpaMessageRepository) {
        this.jpaMessageRepository = jpaMessageRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Message> findById (@PathVariable Integer id){
        Optional<Message> optionalMessage = this.jpaMessageRepository.findById(id);
        if(optionalMessage.isPresent()){
            return ResponseEntity
                    .ok(optionalMessage.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Iterable<Message>> findAll(){
        Iterable<Message> iterable = this.jpaMessageRepository.findAll();
        return ResponseEntity
                .ok(iterable);
    }

    @PostMapping
    public ResponseEntity<Message> save(@RequestBody Message message){
        //validate
        Message data = this.jpaMessageRepository.save(message);
        return ResponseEntity
                .created(URI.create("/api/message"))
                .body(data);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        if(this.jpaMessageRepository.findById(id).isPresent()) {
            this.jpaMessageRepository.deleteById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        else{
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Message> update(@PathVariable Integer id, @RequestBody Message message){
        //validation
        Optional<Message> message1 = this.jpaMessageRepository.findById(id);
        if(message1.isPresent()){
            return ResponseEntity
                    .ok(this.jpaMessageRepository.save(message));
        }
        return ResponseEntity
                .notFound()
                .build();
    }

}
