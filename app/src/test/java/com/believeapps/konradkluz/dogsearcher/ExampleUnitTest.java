package com.believeapps.konradkluz.dogsearcher;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String mydata = "https:\\/\\/dog.ceo\\/api\\/img\\/shihtzu\\/n02086240_10785.jpg";
        String mydata1 = "https://dog.ceo/api/img/dane-great/n02109047_27476.jpg";
        Pattern pattern = Pattern.compile("(?<=img\\/).*?(?=\\s*\\/)");
        Matcher matcher = pattern.matcher(mydata1);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }
}