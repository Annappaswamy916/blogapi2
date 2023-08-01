package com.blogapi1.service.impl;

import com.blogapi1.dto.PostDto;
import com.blogapi1.entity.Post;
import com.blogapi1.exception.ResourceNotFoundException;
import com.blogapi1.repository.PostRepository;
import com.blogapi1.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {


    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepo,ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Post post = mapToPost(postDto);
        Post savedPost = postRepo.save(post);

        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post= postRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());

        return  dto;
    }

    @Override
    public List<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        Page<Post> posts = postRepo.findAll(pageable);
        List<Post> content = posts.getContent();
        List<PostDto> postDtos = content.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );
        postRepo.deleteById(id);

    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(id)
        );

        Post updateContent = mapToPost(postDto);
        updateContent.setId(post.getId());
        Post updatedContentInfo = postRepo.save(updateContent);
        return  mapToDto(updatedContentInfo);

    }

    PostDto mapToDto(Post post){
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
        PostDto dto = modelMapper.map(post,PostDto.class);
        return dto;
    }

    Post mapToPost(PostDto postDto){
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        Post post = modelMapper.map(postDto,Post.class);
        return post;
    }

}
