package emulator;

public class Instruction {
    public short instruction;
    public byte ByteA;
    public byte ByteB;
    public byte NibbleD;
    public byte NibbleC;
    public byte NibbleB;
    public byte NibbleA;
    public short nnn;

    Instruction(short instruction){
        this.instruction = instruction;
        this.ByteA = (byte) (instruction >> 8);
        this.ByteB = (byte) (instruction & 0xFF);
        this.NibbleD = (byte) (instruction & 0xF);
        this.NibbleC = (byte) ((instruction >> 4) & 0xF);
        this.NibbleB = (byte) ((instruction >> 8) & 0xF);
        this.NibbleA = (byte) ((instruction >> 12) & 0xF);
        this.nnn = (short) ((NibbleB<<8) | (NibbleC<<4) | (NibbleD));
    }
}
