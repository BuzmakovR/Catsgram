package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public Collection<Post> findAll(
			@RequestParam(name = "from", defaultValue = "-1") int from,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "asc") String sort) {

		if (SortOrder.from(sort) == null) {
			throw new ParameterNotValidException("sort", "Параметр sort должен содержать корректное значение");
		}
		if (size <= 0) {
			throw new ParameterNotValidException("size", "Параметр size должен быть больше нуля");
		}
		if (from < 0) {
			throw new ParameterNotValidException("from", "Параметр from не может быть меньше нуля");
		}
		return postService.findAll(from, size, SortOrder.from(sort));
	}

	@GetMapping("/{id}")
	public Post findById(@PathVariable("id") long id) {
		return postService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Post create(@RequestBody Post post) {
		return postService.create(post);
	}

	@PutMapping
	public Post update(@RequestBody Post newPost) {
		return postService.update(newPost);
	}

}
