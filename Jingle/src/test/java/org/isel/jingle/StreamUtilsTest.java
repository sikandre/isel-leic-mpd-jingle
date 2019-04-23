package org.isel.jingle;

import org.isel.jingle.model.Artist;
import org.isel.jingle.util.BaseRequest;
import org.isel.jingle.util.StreamUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertEquals;
import static org.isel.jingle.util.StreamUtils.cache;
import static org.junit.Assert.assertArrayEquals;

public class StreamUtilsTest {

    private final List<String> seq1 = Arrays.asList("isel", "ola", "dup", "super", "jingle");
    private final List<Integer> seq2Asc = Arrays.asList(4,5,6,7);
    private final List<Integer> seq2AscDupl = Arrays.asList(4,5,6,7,4,5,6,7);
    private final List<Integer> seq2Desc = Arrays.asList(7,6,5,4);
    private final List<Integer> seq2DescDupl = Arrays.asList(7,6,5,4,7,6,5,4);
    private final List<String> expected = Arrays.asList("isel4", "ola0", "dup0", "super5", "jingle6");

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
        assertEquals(704, artistsSup.get().count());      //700 - prev value
        assertEquals(25, httpGet.count);
        Artist last = artistsSup.get().reduce((p, c) -> c).get();//gets last element
        assertEquals("Coma - Hipertrofia.(2008)", last.getName());
        assertEquals(25, httpGet.count);
    }

    @Test
    public void testMerge(){
        Stream<String> seq1 = Stream.of("isel", "ola", "dup", "super", "jingle");
        Stream<Integer> seq2 = Stream.of(4,5,6,7);
        Supplier<Stream<String>> merge = StreamUtils.merge(seq1, seq2, (s, n) -> s.length() == n, (s, n) -> s + n, 0);
        List<String> res = merge.get().collect(toList());
        List<String> expected = Arrays.asList("isel4", "ola0", "dup0", "super5", "jingle6");

        assertEquals(expected, res);
    }

    @Test
    public void shouldMergeSequencesWithoutDuplicatesOnSeq2Ascending() {
        final List<String> merged = merge(seq2Asc);

        assertEquals(expected, merged);
    }

    @Test
    public void shouldMergeSequencesWithoutDuplicatesOnSeq2AscendingWithDuplicates() {
        final List<String> merged = merge(seq2AscDupl);
        assertEquals(expected, merged);
    }

    @Test
    public void shouldMergeSequencesWithDuplicatesOnSeq2Descending() {
        final List<String> merged = merge(seq2Desc);

        assertEquals(Arrays.asList("isel4", "ola0", "dup0", "super5", "jingle6"), merged);
    }

    @Test
    public void shouldMergeSequencesWithDuplicatesOnSeq2DescendingWithDuplicates() {
        final List<String> merged = merge(seq2DescDupl);

        assertEquals(Arrays.asList("isel4", "ola0", "dup0", "super5", "jingle6"), merged);
    }

    private List<String> merge(List<Integer> seq2) {
        return StreamUtils.merge(seq1.stream(), seq2.stream(), (str, nr) -> str.length() == nr, (str, nr) -> str + nr,0).get().collect(toList());
    }
}
