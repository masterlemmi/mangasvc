package com.lemoncode.manga.request;

import com.lemoncode.validator.AcceptableChapter;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class CreateNewEntryRequest implements Serializable {

    @NotEmpty
    @NotNull
    @AcceptableChapter
    private String chapter;


}