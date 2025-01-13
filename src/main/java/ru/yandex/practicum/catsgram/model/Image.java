package ru.yandex.practicum.catsgram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"id"})
public class Image {

	Long id;
	long postId;
	String originalFileName;
	String filePath;

}
