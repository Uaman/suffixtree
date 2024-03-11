/**
 * Copyright 2012 Alessandro Bahgat Shehata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abahgat.suffixtree;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import junit.framework.TestCase;
import static com.abahgat.suffixtree.Utils.getSubstrings;

public class SuffixTreeTest extends TestCase {

    public static <E> void assertEmpty(Collection<E> collection) {
        assertTrue("Expected empty collection.", collection.isEmpty());
    }

    public static <E> void assertNotEmpty(Collection<E> collection) {
        assertTrue("Expected not empty collection.", !collection.isEmpty());
    }

    public void testBasicTreeGeneration() {
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();

        String word = "cacao";
        in.put(word, 0);

        /* test that every substring is contained within the tree */
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
        assertEmpty(in.search("caco"));
        assertEmpty(in.search("cacaoo"));
        assertEmpty(in.search("ccacao"));

        in = new GeneralizedSuffixTree();
        word = "bookkeeper";
        in.put(word, 0);
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
        assertEmpty(in.search("books"));
        assertEmpty(in.search("boke"));
        assertEmpty(in.search("ookepr"));
    }

    public void testStartsWith() {//check empty cases
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
        assertEmpty(in.startsWith("caco"));
        assertEmpty(in.startsWith("cacaoo"));
        assertEmpty(in.startsWith("ccacao"));

        in = new GeneralizedSuffixTree();
        word = "bookkeeper";
        in.put(word, 0);
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
        assertEmpty(in.startsWith("books"));
        assertEmpty(in.startsWith("boke"));
        assertEmpty(in.startsWith("ookepr"));
    }

    public void testStartsWith2() {//check non-empty cases
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);

        assertNotEmpty(in.startsWith("cacao"));
        assertNotEmpty(in.startsWith("cac"));
        assertNotEmpty(in.startsWith("ca"));
        assertNotEmpty(in.startsWith("c"));
        assertEmpty(in.startsWith(""));
        assertEmpty(in.startsWith("^"));
    }

    public void testStartsWith3() {//check non-empty cases with different words
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "bookkeeper";
        in.put(word, 0);
        in.put("coconut", 1);
        in.put("br", 2);


        assertNotEmpty(in.startsWith("bookkeeper"));
        assertNotEmpty(in.startsWith("bookkeepe"));
        assertNotEmpty(in.startsWith("bookkeep"));
        assertNotEmpty(in.startsWith("bookkee"));
        assertNotEmpty(in.startsWith("bookke"));
        assertNotEmpty(in.startsWith("bookk"));
        assertNotEmpty(in.startsWith("book"));
        assertNotEmpty(in.startsWith("boo"));
        assertNotEmpty(in.startsWith("bo"));
        assertNotEmpty(in.startsWith("b"));
        assertEmpty(in.startsWith(""));

        Collection<Integer> result = in.startsWith("b");
        assertTrue(result.size() == 2);
        assertTrue(result.contains(0));
        assertTrue(result.contains(2));

        assertNotEmpty(in.startsWith("c"));
        assertEmpty(in.startsWith("er"));
        assertEmpty(in.startsWith("r"));
        assertEmpty(in.startsWith("t"));
        assertEmpty(in.startsWith("^"));
    }

    public void testEndsWith() {//check empty cases
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
        assertEmpty(in.endsWith("caco"));
        assertEmpty(in.endsWith("cacaoo"));
        assertEmpty(in.endsWith("ccacao"));

        in = new GeneralizedSuffixTree();
        word = "bookkeeper";
        in.put(word, 0);
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
        assertEmpty(in.endsWith("books"));
        assertEmpty(in.endsWith("boke"));
        assertEmpty(in.endsWith("ookepr"));
    }

    public void testEndsWith2() {//check non-empty cases
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);

        assertNotEmpty(in.endsWith("cacao"));
        assertNotEmpty(in.endsWith("acao"));
        assertNotEmpty(in.endsWith("cao"));
        assertNotEmpty(in.endsWith("ao"));
        assertNotEmpty(in.endsWith("o"));
        assertEmpty(in.endsWith(""));
        assertEmpty(in.endsWith("$"));
    }

    public void testEndsWith3() {//check non-empty cases with different words
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        in.put("bookkeeper", 0);
        in.put("coconut", 1);
        in.put("br", 2);

        assertNotEmpty(in.endsWith("bookkeeper"));
        assertNotEmpty(in.endsWith("ookkeeper"));
        assertNotEmpty(in.endsWith("okkeeper"));
        assertNotEmpty(in.endsWith("kkeeper"));
        assertNotEmpty(in.endsWith("keeper"));
        assertNotEmpty(in.endsWith("eeper"));
        assertNotEmpty(in.endsWith("eper"));
        assertNotEmpty(in.endsWith("per"));
        assertNotEmpty(in.endsWith("er"));
        assertNotEmpty(in.endsWith("r"));
        assertEmpty(in.endsWith(""));
        assertEmpty(in.endsWith("$"));

        Collection<Integer> result = in.endsWith("r");
        assertTrue(result.size() == 2);
        assertTrue(result.contains(0));
        assertTrue(result.contains(2));
        assertTrue(false == result.contains(1));
    }

    public void testSearchWord() {//check empty cases
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);

        assertEmpty(in.searchWord("caco"));
        assertEmpty(in.searchWord("cacaoo"));
        assertEmpty(in.searchWord("ccacao"));
        assertEmpty(in.searchWord("cacaoo"));
        assertEmpty(in.searchWord("ccacao"));
        assertEmpty(in.searchWord("caca"));
        assertEmpty(in.searchWord("acao"));
        assertEmpty(in.searchWord("cao"));
        assertEmpty(in.searchWord(""));
    }

    public void testSearchWord2() {//check non-empty cases
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);

        assertNotEmpty(in.searchWord("cacao"));
        assertNotEmpty(in.searchWord(word));
    }

    public void testSearchWord3() {//check non-empty cases with different words
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        in.put("bookkeeper", 0);
        in.put("coconut", 1);
        in.put("br", 2);
        in.put("br", 3);

        assertNotEmpty(in.searchWord("bookkeeper"));
        assertEmpty(in.searchWord("books"));
        assertEmpty(in.searchWord("boke"));
        assertEmpty(in.searchWord("ookepr"));
        assertNotEmpty(in.searchWord("coconut"));
        assertNotEmpty(in.searchWord("br"));
        assertEmpty(in.searchWord("r"));
        assertEmpty(in.searchWord("t"));
        assertEmpty(in.searchWord(""));

        Collection<Integer> result = in.searchWord("br");
        assertTrue(result.size() == 2);
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
        assertTrue(false == result.contains(0));
    }

    public void testWeirdword() {
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();

        String word = "cacacato";
        in.put(word, 0);

        /* test that every substring is contained within the tree */
        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
        }
    }

    public void testDouble() {
        // test whether the tree can handle repetitions
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String word = "cacao";
        in.put(word, 0);
        in.put(word, 1);

        for (String s : getSubstrings(word)) {
            assertTrue(in.search(s).contains(0));
            assertTrue(in.search(s).contains(1));
        }
    }

    public void testBananaAddition() {
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String[] words = new String[] {"banana", "bano", "ba"};
        for (int i = 0; i < words.length; ++i) {
            in.put(words[i], i);

            for (String s : getSubstrings(words[i])) {
                Collection<Integer> result = in.search(s);
                assertNotNull("result null for string " + s + " after adding " + words[i], result);
                assertTrue("substring " + s + " not found after adding " + words[i], result.contains(i));
            }

        }

        // verify post-addition
        for (int i = 0; i < words.length; ++i) {
            for (String s : getSubstrings(words[i])) {
                assertTrue(in.search(s).contains(i));
            }
        }

        // add again, to see if it's stable
        for (int i = 0; i < words.length; ++i) {
            in.put(words[i], i + words.length);

            for (String s : getSubstrings(words[i])) {
                assertTrue(in.search(s).contains(i + words.length));
            }
        }

    }

    public void testAddition() {
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String[] words = new String[] {"cacaor" , "caricato", "cacato", "cacata", "caricata", "cacao", "banana"};
        for (int i = 0; i < words.length; ++i) {
            in.put(words[i], i);

            for (String s : getSubstrings(words[i])) {
                Collection<Integer> result = in.search(s);
                assertNotNull("result null for string " + s + " after adding " + words[i], result);
                assertTrue("substring " + s + " not found after adding " + words[i], result.contains(i));
            }
        }
        // verify post-addition
        for (int i = 0; i < words.length; ++i) {
            for (String s : getSubstrings(words[i])) {
                Collection<Integer> result = in.search(s);
                assertNotNull("result null for string " + s + " after adding " + words[i], result);
                assertTrue("substring " + s + " not found after adding " + words[i], result.contains(i));
            }
        }

        // add again, to see if it's stable
        for (int i = 0; i < words.length; ++i) {
            in.put(words[i], i + words.length);

            for (String s : getSubstrings(words[i])) {
                assertTrue(in.search(s).contains(i + words.length));
            }
        }
        
        in.computeCount();
        testResultsCount(in.getRoot());

        assertEmpty(in.search("aoca"));
    }

    public void testSampleAddition() {
        GeneralizedSuffixTree in = new GeneralizedSuffixTree();
        String[] words = new String[] {"libertypike",
            "franklintn",
            "carothersjohnhenryhouse",
            "carothersezealhouse",
            "acrossthetauntonriverfromdightonindightonrockstatepark",
            "dightonma",
            "dightonrock",
            "6mineoflowgaponlowgapfork",
            "lowgapky",
            "lemasterjohnjandellenhouse",
            "lemasterhouse",
            "70wilburblvd",
            "poughkeepsieny",
            "freerhouse",
            "701laurelst",
            "conwaysc",
            "hollidayjwjrhouse",
            "mainandappletonsts",
            "menomoneefallswi",
            "mainstreethistoricdistrict",
            "addressrestricted",
            "brownsmillsnj",
            "hanoverfurnace",
            "hanoverbogironfurnace",
            "sofsavannahatfergusonaveandbethesdard",
            "savannahga",
            "bethesdahomeforboys",
            "bethesda"};
        for (int i = 0; i < words.length; ++i) {
            in.put(words[i], i);

            for (String s : getSubstrings(words[i])) {
                Collection<Integer> result = in.search(s);
                assertNotNull("result null for string " + s + " after adding " + words[i], result);
                assertTrue("substring " + s + " not found after adding " + words[i], result.contains(i));
            }


        }
        // verify post-addition
        for (int i = 0; i < words.length; ++i) {
            for (String s : getSubstrings(words[i])) {
                assertTrue(in.search(s).contains(i));
            }
        }

        // add again, to see if it's stable
        for (int i = 0; i < words.length; ++i) {
            in.put(words[i], i + words.length);

            for (String s : getSubstrings(words[i])) {
                assertTrue(in.search(s).contains(i + words.length));
            }
        }

        in.computeCount();
        testResultsCount(in.getRoot());

        assertEmpty(in.search("aoca"));
    }

    private void testResultsCount(Node n) {
        for (Edge e : n.getEdges().values()) {
            assertEquals(n.getData(-1).size(), n.getResultCount());
            testResultsCount(e.getDest());
        }
    }

    /* testing a test method :) */
    public void testGetSubstrings() {
        Collection<String> exp = new HashSet<String>();
        exp.addAll(Arrays.asList(new String[] {"w", "r", "d", "wr", "rd", "wrd"}));
        Collection<String> ret = getSubstrings("wrd");
        assertTrue(ret.equals(exp));
    }

}
