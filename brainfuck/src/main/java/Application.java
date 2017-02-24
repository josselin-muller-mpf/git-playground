import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

public class Application
{
    public static void main(String[] args) throws IOException
    {
        byte[] data = new byte[30000];
        Arrays.fill(data, (byte)0);

        String cmd = args[0];
        int ptr = 0;
        int index = 0;

        Stack<Integer> loops = new Stack<Integer>();

        while (index < cmd.length())
        {
            char c = cmd.charAt(index);

            switch (c)
            {
                case '>':
                    ++ptr;
                    break;

                case '<':
                    --ptr;
                    break;

                case '+':
                    data[ptr] += 1;
                    break;

                case '-':
                    data[ptr] -= 1;
                    break;

                case '.':
                    System.out.print((char)data[ptr]);
                    break;

                case ',':
                    data[ptr] = (byte) System.in.read();
                    break;

                case '[':
                    if (data[ptr] == 0)
                        index = getMatchingEnd(cmd, index);
                    else
                        loops.push(index);
                    break;

                case ']':
                    if (data[ptr] != 0)
                        index = loops.peek();
                    else
                        loops.pop();
                    break;
            }

            ++index;
        }
    }

    private static int getMatchingEnd(String cmd, int index)
    {
        int count = 0;
        while (index < cmd.length())
        {
            char c = cmd.charAt(index);

            if (c == '[')
                ++count;
            else if (c == ']')
            {
                if (count == 0)
                    return index;
                --count;
            }
            ++index;
        }
        return index;
    }
}
