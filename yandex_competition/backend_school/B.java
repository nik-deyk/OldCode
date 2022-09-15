import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class B {

    static class Participant implements Comparable<Participant> {
        public final String name;
        public final long priority;

        public Participant(String name, int performanceCount, int fine) {
            this.name = name;
            this.priority = performanceCount * 1000000001L + (1000000000L - fine);
        }

        @Override
        public int compareTo(B.Participant other) {
            return Long.compare(this.priority, other.priority);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Participant other = (Participant) obj;
            return this.priority == other.priority;
        }
    }

    static class Discipline {
        public final short maxParticipant;

        private final TreeSet<Participant> participants = new TreeSet<>();

        public Discipline(short maxParticipant) {
            this.maxParticipant = maxParticipant;
        }

        public void addParticipantIfNotFull(Participant participant) {
            if (participants.size() < maxParticipant) {
                participants.add(participant);
            } else if (participants.first().compareTo(participant) < 0) {
                participants.remove(participants.first());
                participants.add(participant);
            }
        }

        public Collection<Participant> getParticipants() {
            return this.participants;
        }

        public void clear() {
            participants.clear();
        }
    }

    static final HashMap<Integer, Discipline> disciplines = new HashMap<Integer, Discipline>();

    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            int disciplineCount = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < disciplineCount; i++) {
                String[] disciplineInfo = bufferedReader.readLine().split(",");
                disciplines.put(disciplineInfo[0].hashCode(), new Discipline(Short.parseShort(disciplineInfo[1])));
            }

            int participantCount = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < participantCount; i++) {
                String[] participantInfo = bufferedReader.readLine().split(",");
                int disciplineHash = participantInfo[1].hashCode();
                Participant participant = new Participant(participantInfo[0], Integer.parseInt(participantInfo[2]),
                        Integer.parseInt(participantInfo[3]));
                // disciplineHash is 100% in map:
                Discipline discipline = disciplines.get(disciplineHash);
                discipline.addParticipantIfNotFull(participant);
            }

            bufferedReader.close();

            // Prepare for output:
            TreeSet<Participant> result = new TreeSet<>(Comparator.comparing((Participant p) -> p.name));
            disciplines.forEach((Integer hash, Discipline discipline) -> {
                result.addAll(discipline.getParticipants());
                discipline.clear();
            });

            final BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(System.out));
            result.forEach(participant -> {
                try {
                    writter.write(participant.name + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writter.flush();
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}