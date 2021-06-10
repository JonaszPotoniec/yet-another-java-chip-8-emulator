package emulator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Memory {
    private final byte[] RAM;
    private final short[] stack;

    Memory(File romFile) throws Exception {
        this.RAM = new byte[0xFFF];
        this.stack = new short[0xF];

        try{
            loadROM(romFile);
        } catch (IOException e) {
            System.err.println("Nie można wczytać programu!");
            throw new Exception("Nie można wczytać programu!");
        }

        loadHexDigits();
    }

    private void loadROM(File romFile) throws IOException {
        byte[] rom = Files.readAllBytes(romFile.toPath());
        int size = rom.length;
        if(size > 0xFFF - 0x200) size = 0xFFF - 0x200;
        System.arraycopy(rom, 0, RAM, 0x200, size);
    }

    public byte getRAM(int index){
        if(index > 0xFFF){
            System.err.println("Próba odczytania adresu " + index + "!");
            return 0;
        }

        return RAM[index];
    }

    public short getRAMShort(int index){
        if(index > 0xFFF){
            System.err.println("Próba odczytania adresu " + index + "!");
            return 0;
        }

        return (short) (((short)RAM[index] << 8) + ((short)RAM[index+1] & 0x00FF));
    }

    public void setRAM(int index, byte data){
        if(index > 0xFFF){
            System.err.println("Próba zapisania adresu " + index + "!");
        } else {
            RAM[index] = data;
        }
    }
    public byte[] getAllRAM(){
        return RAM;
    }

    public short getStack(int index){
        if(index > 0xF || index < 0){
            System.err.println("Próba odczytania adresu do stosu " + index + "!");
            return 0;
        }

        return stack[index];
    }

    public void setStack(int index, short data){
        if(index > 0xF || index < 0){
            System.err.println("Próba zapisania adresu do stosu " + index + "!");
        } else {
            stack[index] = data;
        }
    }

    private void loadHexDigits(){
        byte []hexDigits = new byte[]{
                (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xf0,//0
                (byte) 0x20, (byte) 0x60, (byte) 0x20, (byte) 0x20, (byte) 0x70,//1
                (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0,//2
                (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x10, (byte) 0xF0,//3
                (byte) 0x90, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0x10,//4
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x10, (byte) 0xF0,//5
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0,//6
                (byte) 0xF0, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x40,//7
                (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0,//8
                (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0xF0,//9
                (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90,//A
                (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0,//B
                (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0,//C
                (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0,//D
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0,//E
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80 //F
        };

        System.arraycopy(hexDigits, 0, RAM, 0, 0x10 * 0x5);
    }
}
