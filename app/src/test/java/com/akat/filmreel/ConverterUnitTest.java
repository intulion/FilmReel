package com.akat.filmreel;

import com.akat.filmreel.data.local.IntListConverter;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConverterUnitTest {

    private List<Integer> list;
    private String stringList;

    @Before
    public void setUp() {
        list = Arrays.asList(10, 52, 1);
        stringList = "10,52,1";
    }

    @Test
    public void toList() {
        assertEquals(list, IntListConverter.toList(stringList));
    }

    @Test
    public void OneItemToList() {
        List<Integer> list = Collections.singletonList(10);
        assertEquals(list, IntListConverter.toList("10"));
    }

    @Test
    public void EmptyToList() {
        assertNull(IntListConverter.toList(null));
        assertNull(IntListConverter.toList(""));
    }

    @Test
    public void fromList() {
        assertEquals(stringList, IntListConverter.fromList(list));
    }

    @Test
    public void OneItemFromList() {
        List<Integer> list = Collections.singletonList(10);
        assertEquals("10", IntListConverter.fromList(list));
    }

    @Test
    public void EmptyFromList() {
        assertNull(IntListConverter.fromList(null));
        assertNull(IntListConverter.fromList(Collections.emptyList()));
    }
}