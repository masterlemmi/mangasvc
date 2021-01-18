package com.lemoncode.manga.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class UpdateReadStatusRequest implements Serializable {

    @NotEmpty
    private Long id;
    @NotEmpty
    private boolean done;


}