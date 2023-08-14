package com.github.tamadalab.nikusa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * MainTest
 */
public class MainTest
{
    @Test
    public void testMain()
    {
        assertTrue(Main.project == "nikusa");
    }
}