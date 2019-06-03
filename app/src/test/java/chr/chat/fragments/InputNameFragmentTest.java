package chr.chat.fragments;

import org.junit.Test;

import static org.junit.Assert.*;

public class InputNameFragmentTest {

    private InputNameFragment fragment = new InputNameFragment();

    @Test
    public void checkText() {
        String input = "";
        boolean result;
        boolean expected = true;

        result = fragment.checkText(input);

        assertEquals(expected, result);

    }
}