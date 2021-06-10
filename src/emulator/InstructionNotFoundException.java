package emulator;

public class InstructionNotFoundException extends Exception{
    public Instruction instruction;

    public InstructionNotFoundException(Instruction instruction){
        super("Unknown opcode " + instruction + "!");
        this.instruction = instruction;
    }
}
