import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Dedicated to reading user input character by character and maintaining a buffer
// Required for making sure the input doesn't look wonky, scanner.nextLine() doesn't cut it
public class BufferedCharInput {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static StringBuilder inputBuffer = new StringBuilder();

    private static String inPrompt;



    public static String readLineAtomic(String prompt) throws IOException
    {
        // Clear
        inputBuffer.setLength(0);

        inPrompt = prompt;
        System.out.print(prompt);

        while(true)
        {
            int chCode = reader.read();

            if(chCode == -1) // EOF
                break;

            char ch = (char)chCode;

            if(ch == '\n' || ch == '\r') // Enter, commit buffer
            {
                String result = inputBuffer.toString();
                inputBuffer.setLength(0); // Clear buffer

                return result;
            }
            else if (ch == '\b') // Backspace
            {
                if(!inputBuffer.isEmpty())
                {
                    inputBuffer.deleteCharAt(inputBuffer.length()-1);
                    System.out.print("\b \b");
                }
            }
            else if (ch >= 32 && ch <= 126) // Printable
            {
                inputBuffer.append(ch);
            }

        }
        return inputBuffer.toString();
    }

    public static void clearLine()
    {
        // Move cursor back and overwrite with whitespace
        int length = inputBuffer.length() + inPrompt.length();
        for (int i = 0; i < length; i++) {
            System.out.print("\b \b");
        }
    }
    public static void redrawLine()
    {
        System.out.print(inPrompt + inputBuffer.toString());
    }
}
