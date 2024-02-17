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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A specialized implementation of Map that uses native char types and sorted
 * arrays to keep minimize the memory footprint.
 * Implements only the operations that are needed within the suffix tree context.
 */
class EdgeBag implements Map<Character, Edge> {
    private byte[][] chars;
    private Edge[][] values;
    private static final int BSEARCH_THRESHOLD = 6;
    private static final int UTF_16BE = 8;

    @Override
    public Edge put(Character character, Edge e) {
        char c = character.charValue();
        byte[] cBytes = getBytesFromChar(c);
        byte cByte0 = cBytes[0];
        byte cByte1 = cBytes[1];

        if (!isLegalArgument(c, cBytes)) {
            throw new IllegalArgumentException("Illegal input character " + c + ".");
        }
        
        if (chars == null) {
            chars = new byte[UTF_16BE][0];
            values = new Edge[UTF_16BE][0];
        }
        
        byte[] currentChars = chars[cByte0];
        Edge[] currentValues = values[cByte0];        
        if (currentChars == null) {
            currentChars = new byte[0];
            chars[cByte0] = currentChars;
            currentValues = new Edge[0];
            values[cByte0] = currentValues;
        }

        int idx = search(c, cBytes);
        Edge previous = null;

        if (idx < 0) {
            int currsize = currentChars.length;
            byte[] copy = new byte[currsize + 1];
            System.arraycopy(currentChars, 0, copy, 0, currsize);
            currentChars = copy;
            chars[cByte0] = currentChars;
            Edge[] copy1 = new Edge[currsize + 1];
            System.arraycopy(currentValues, 0, copy1, 0, currsize);
            currentValues = copy1;
            values[cByte0] = currentValues;
            currentChars[currsize] = cByte1;
            currentValues[currsize] = e;
            currsize++;
            if (currsize > BSEARCH_THRESHOLD) {
                sortArrays(cByte0);
            }
        } else {
            previous = currentValues[idx];
            currentValues[idx] = e;
        }
        return previous;
    }
    
    @Override
    public Edge get(Object maybeCharacter) {
        return get(((Character) maybeCharacter).charValue());  // throws if cast fails.
    }

    public Edge get(char c) {
        byte[] cBytes = getBytesFromChar(c);
        if (!isLegalArgument(c, cBytes)) {
            throw new IllegalArgumentException("Illegal input character " + c + ".");
        }
        
        int idx = search(c, cBytes);
        if (idx < 0) {
            return null;
        }
        return values[cBytes[0]][idx];
    }

    private byte[] getBytesFromChar(char c) {
        return String.valueOf(c).getBytes(StandardCharsets.UTF_16BE);
    }

    private char getCharFromBytes(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_16BE).charAt(0);
    }

    private boolean isLegalArgument(char c, byte[] cBytes) {
        return cBytes.length == 2 || getCharFromBytes(cBytes) == c;
    }

    private int search(char c, byte[] bytes) {
        if (chars == null)
            return -1;
        
        byte[] currentChars = chars[bytes[0]];
        if (currentChars.length > BSEARCH_THRESHOLD) {
            return java.util.Arrays.binarySearch(currentChars, bytes[1]);
        }

        for (int i = 0; i < currentChars.length; i++) {
            if (c == currentChars[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Collection<Edge> values() {
        if (values == null) {
            return Arrays.asList(new Edge[0]);
        }
        List<Edge> allValues = new ArrayList<Edge>();
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                for (int j = 0; j < values[i].length; j++) {
                    allValues.add(values[i][j]);
                }
            }
        }
        return allValues;
    }
    
    /**
     * A trivial implementation of sort, used to sort chars[] and values[] according to the data in chars.
     * 
     * It was preferred to faster sorts (like qsort) because of the small sizes (<=36) of the collections involved.
     */
    private void sortArrays(int idx) {
        byte[] currentChars = chars[idx];
        Edge[] currentValues = values[idx];
        for (int i = 0; i < currentChars.length; i++) {
         for (int j = i; j > 0; j--) {
            if (currentChars[j-1] > currentChars[j]) {
               byte swap = currentChars[j];
               currentChars[j] = currentChars[j-1];
               currentChars[j-1] = swap;

               Edge swapEdge = currentValues[j];
               currentValues[j] = currentValues[j-1];
               currentValues[j-1] = swapEdge;
            }
         }
      }
    }
    
    @Override
    public boolean isEmpty() {
        return chars == null || chars.length == 0 || chars[0].length == 0;
    }
    
    @Override
    public int size() {
        if (chars == null) {
            return 0;
        }
        int size = 0;
        for (int i = 0; i < chars.length; i++) {
            size += chars[i].length;
        }
        return size;
    }
    
    @Override
    public Set<Map.Entry<Character, Edge>> entrySet() {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public Set<Character> keySet() {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public void putAll(Map<? extends Character, ? extends Edge> m) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public Edge remove(Object key) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public boolean containsValue(Object key) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
