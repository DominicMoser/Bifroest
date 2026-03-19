package com.dmoser.codyssey.bifroest;

import com.dmoser.codyssey.bifroest.structure.Test2DTO;
import java.util.List;

public record TestDTO(String test, List<Test2DTO> test2) {}
