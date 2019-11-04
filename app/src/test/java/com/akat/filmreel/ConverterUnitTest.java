package com.akat.filmreel;

import com.akat.filmreel.data.db.IntListConverter;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ConverterUnitTest {
    @Test
    public void toList_isCorrect() {
        List<Integer> list = Arrays.asList(10, 52, 1);
        assertEquals(list, IntListConverter.toList("10,52,1"));
    }

    @Test
    public void fromList_isCorrect() {
        List<Integer> list = Arrays.asList(10, 52, 1);
        assertEquals("10,52,1", IntListConverter.fromList(list));
    }
}