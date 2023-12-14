import java.util.*;
import java.util.stream.Collectors;

public class SpringRow {
    private final String mask;
    private final List<Integer> groups;
    private final int mask_hashmark_count;
    private final int mask_question_count;
    private final int groups_hashmark_count;
    private final String groups_str;

    public SpringRow(String l) {
        var ll = l.split(" ");
        this.mask = ll[0];
        groups = Arrays.stream(ll[1].split(",")).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
        mask_hashmark_count = (int)mask.chars().filter(c->c=='#').count();
        mask_question_count = (int)mask.chars().filter(c->c=='?').count();
        groups_hashmark_count = groups.stream().mapToInt(Integer::valueOf).sum();
        groups_str = groups.stream().map(i->".>"+"#".repeat(i-1)).collect(Collectors.joining()).substring(1);

        //System.out.println(l);
        //System.out.println(String.format("  %s  %s",mask,groups_str));
    }

    public long countMatches() {
        //System.out.print(String.format("  %s  %s  = ",mask,groups_str));
        int change_question_to_hashmark_count = groups_hashmark_count-mask_hashmark_count;
        int change_question_to_dot_count = mask_question_count - change_question_to_hashmark_count;
        long variants = countMatches(0,0,change_question_to_hashmark_count,change_question_to_dot_count);
        //System.out.println(variants);
        return variants;
    }

    Map<Long,Long> cache = new TreeMap<>();
    private long countMatches(int maskpos, int grstrpos, int change_question_to_hashmark_count, int change_question_to_dot_count) {
        Long cacheKey = maskpos + 100L*grstrpos + 10000L*change_question_to_hashmark_count + 1000000L*change_question_to_dot_count;
        Long cacheValue = cache.get(cacheKey);
        if (cacheValue!=null)
            return cacheValue;
        if (maskpos == mask.length()) {
            return 1L;
        }
        Character mask_c = mask.charAt(maskpos);
        Character grstr_c = grstrpos<groups_str.length() ? groups_str.charAt(grstrpos) : '.';
        long variants = 0L;
        // ?#. >#.
        if (mask_c=='?') {
            if (grstr_c=='>' && change_question_to_hashmark_count>0) {
                variants += countMatches(maskpos+1,grstrpos+1,change_question_to_hashmark_count-1,change_question_to_dot_count);
            }
            if (grstr_c=='>' && change_question_to_dot_count>0) {
                variants += countMatches(maskpos+1,grstrpos,change_question_to_hashmark_count,change_question_to_dot_count-1);
            }
            if (grstr_c=='#' && change_question_to_hashmark_count>0) {
                variants += countMatches(maskpos+1,grstrpos+1,change_question_to_hashmark_count-1,change_question_to_dot_count);
            }
            if (grstr_c=='.' && change_question_to_dot_count>0) {
                variants += countMatches(maskpos+1,grstrpos+1,change_question_to_hashmark_count,change_question_to_dot_count-1);
            }
        } else if (mask_c=='#') {
            if (grstr_c=='>' || grstr_c=='#') {
                variants += countMatches(maskpos+1,grstrpos+1,change_question_to_hashmark_count,change_question_to_dot_count);
            }
        } else if (mask_c=='.') {
            if (grstr_c=='>') {
                variants += countMatches(maskpos+1,grstrpos,change_question_to_hashmark_count,change_question_to_dot_count);
            }
            if (grstr_c=='.') {
                variants += countMatches(maskpos+1,grstrpos+1,change_question_to_hashmark_count,change_question_to_dot_count);
            }
        }
        cache.put(cacheKey,variants);
        return variants;
    }
}
