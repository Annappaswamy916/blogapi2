package com.blogapi1.service.impl;

import com.blogapi1.dto.CommentDto;
import com.blogapi1.entity.Comment;
import com.blogapi1.entity.Post;
import com.blogapi1.exception.ResourceNotFoundException;
import com.blogapi1.repository.CommentRepository;
import com.blogapi1.repository.PostRepository;
import com.blogapi1.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepo;
    private CommentRepository commentRepo;

    public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException(postId)
        );
        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        comment.setPost(post);
        Comment savedPost = commentRepo.save(comment);
        CommentDto dto = new CommentDto();
        dto.setId(savedPost.getId());
        dto.setName(savedPost.getName());
        dto.setEmail(savedPost.getEmail());
        dto.setBody(savedPost.getBody());
        return dto;
    }

    @Override
    public List<CommentDto> findCommentByPostId(long postId) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public void deleteCommentById(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );
        commentRepo.deleteById(id);
    }

    @Override
    public CommentDto getCommentById(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );
        CommentDto dto = mapToDto(comment);
        return dto;
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException(postId)
        );
        Comment comment = commentRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepo.save(comment);
        CommentDto dto = mapToDto(updatedComment);

        return dto;
    }

    CommentDto mapToDto (Comment comment){
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setName(comment.getName());
        dto.setEmail(comment.getEmail());
        dto.setBody(comment.getBody());
        return dto;
    }
}
