package com.blogapi1.controller;

import com.blogapi1.dto.CommentDto;
import com.blogapi1.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("postId") long postId,
            @RequestBody CommentDto commentDto
    ){
        CommentDto dto = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> findCommentByPostId(@PathVariable(value = "postId") long postId){
        List<CommentDto> dtos = commentService.findCommentByPostId(postId);
        return dtos;
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("postId") long postId,@PathVariable("id")long id){
        commentService.deleteCommentById(postId,id);
        return new ResponseEntity<>("Comment is deleted",HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") long postId,@PathVariable("id") long id){
        CommentDto dto = commentService.getCommentById(postId,id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id,
            @RequestBody CommentDto commentDto
    ){
        CommentDto updatedComment = commentService.updateComment(postId,id,commentDto);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

}
