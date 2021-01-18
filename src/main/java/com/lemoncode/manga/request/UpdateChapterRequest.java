package com.lemoncode.manga.request;

import com.lemoncode.validator.AcceptableChapter;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class UpdateChapterRequest implements Serializable {

    @NotEmpty
    private Long id;
    @NotEmpty
    @NotNull
    @AcceptableChapter
    private String chapter;


}