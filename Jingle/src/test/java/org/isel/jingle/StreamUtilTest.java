package org.isel.jingle;

import org.isel.jingle.model.Artist;
import org.isel.jingle.util.BaseRequest;
import org.junit.Test;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static junit.framework.Assert.assertEquals;
import static org.isel.jingle.util.StreamUtil.cache;
import static org.junit.Assert.assertArrayEquals;

public class StreamUtilTest {

    @Test
    public void testCache() {
        Stream<Integer> nrs = Stream.generate( () -> (new Random()).nextInt(100) );
        Supplier<Stream<Integer>> cache = cache(nrs.limit(10));
        Object[] expected = cache.get().toArray();
        Object[] actual = cache.get().toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void searchHiperAndCountAllResultsWithCaching() {
        JingleServiceTest.HttpGet httpGet = new JingleServiceTest.HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Stream<Artist> artists =  service.searchArtist("hiper");
        Supplier<Stream<Artist>> artistsSup = cache(artists);
        assertEquals(0, httpGet.count);
        assertEquals(702, artistsSup.get().count());      //700 - prev value
        assertEquals(25, httpGet.count);
        Artist last = artistsSup.get().reduce((p, c) -> c).get();//gets last element
        assertEquals("Coma - Hipertrofia.(2008)", last.getName());
        assertEquals(25, httpGet.count);
    }
}
