/* The following code was generated by JFlex 1.7.0 tweaked for IntelliJ platform */

/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>Obj.flex</tt>
 */
class ObjLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int WAITING_STRING = 2;
  public static final int WAITING_FLOAT = 4;
  public static final int WAITING_FACE_VERTEX = 6;
  public static final int WAITING_VERTEX_INDEX = 8;
  public static final int WAITING_FLAG = 10;
  public static final int WAITING_REFERENCE = 12;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6, 6
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [9, 6, 6]
   * Total runtime size is 3872 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch>>12]<<6)|((ch>>6)&0x3f)]<<6)|(ch&0x3f)];
  }

  /* The ZZ_CMAP_Z table has 272 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\1\1\1\2\7\3\1\4\4\3\1\5\1\6\1\7\4\3\1\10\6\3\1\11\1\12\361\3");

  /* The ZZ_CMAP_Y table has 704 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\1\2\26\3\1\4\1\3\1\5\3\3\1\6\5\3\1\7\1\3\1\7\1\3\1\7\1\3\1\7\1\3"+
    "\1\7\1\3\1\7\1\3\1\7\1\3\1\7\1\3\1\7\1\3\1\7\1\3\1\10\1\3\1\10\1\4\4\3\1\6"+
    "\1\10\34\3\1\4\1\10\4\3\1\11\1\3\1\10\2\3\1\12\2\3\1\10\1\5\2\3\1\12\16\3"+
    "\1\13\227\3\1\4\12\3\1\10\1\6\2\3\1\14\1\3\1\10\5\3\1\5\114\3\1\10\25\3\1"+
    "\4\56\3\1\7\1\3\1\5\1\15\2\3\1\10\3\3\1\5\5\3\1\10\1\3\1\10\5\3\1\10\1\3\1"+
    "\6\1\5\6\3\1\4\15\3\1\10\67\3\1\4\3\3\1\10\61\3\1\16\105\3\1\10\32\3");

  /* The ZZ_CMAP_A table has 960 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\4\1\2\1\1\2\2\22\0\1\4\2\0\1\3\7\0\1\32\1\0\1\24\1\30\1\15\1\25\1"+
    "\33\10\26\13\0\1\31\34\0\1\21\2\0\1\23\1\12\1\6\1\0\1\20\2\0\1\13\1\17\1\11"+
    "\1\5\1\14\2\0\1\16\1\10\1\22\1\7\16\0\1\1\232\0\12\27\106\0\12\27\6\0\12\27"+
    "\134\0\12\27\40\0\12\27\54\0\12\27\60\0\12\27\6\0\12\27\116\0\2\1\46\0\12"+
    "\27\26\0\12\27\74\0\12\27\16\0\62\27");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\7\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7"+
    "\1\10\1\11\1\12\2\1\2\13\1\14\1\15\1\1"+
    "\2\16\1\17\1\20\1\21\1\22\1\23\1\24\1\1"+
    "\1\25\2\26\1\27\1\30\1\31\10\0\2\16\4\0"+
    "\1\32\1\33";

  private static int [] zzUnpackAction() {
    int [] result = new int[55];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\34\0\70\0\124\0\160\0\214\0\250\0\304"+
    "\0\340\0\374\0\304\0\304\0\u0118\0\304\0\304\0\304"+
    "\0\304\0\u0134\0\u0150\0\u016c\0\u0188\0\u01a4\0\u01c0\0\u01dc"+
    "\0\u01f8\0\u0214\0\u0230\0\304\0\u024c\0\u0268\0\u0284\0\u02a0"+
    "\0\u02bc\0\304\0\u02d8\0\u02f4\0\u0310\0\304\0\304\0\u032c"+
    "\0\u0348\0\u0364\0\u0380\0\u039c\0\u03b8\0\u03d4\0\u03f0\0\u03f0"+
    "\0\u040c\0\u0428\0\u0444\0\u0460\0\u047c\0\304\0\304";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[55];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\10\2\11\1\12\1\11\1\13\1\14\1\15\2\10"+
    "\1\16\1\17\1\20\1\10\1\21\1\22\2\10\1\23"+
    "\11\10\1\24\1\25\1\11\1\24\1\26\27\24\1\10"+
    "\2\11\1\10\1\27\17\10\1\30\1\31\1\32\4\10"+
    "\1\32\1\10\2\11\1\10\1\33\10\10\1\34\10\10"+
    "\1\35\4\10\1\35\1\10\2\11\1\10\1\36\21\10"+
    "\1\37\4\10\1\37\1\10\2\11\1\10\1\40\1\41"+
    "\25\10\1\42\1\43\1\44\1\11\1\43\1\45\27\43"+
    "\35\0\2\11\1\0\1\11\27\0\2\12\1\0\31\12"+
    "\10\0\1\46\1\47\32\0\1\50\41\0\1\51\15\0"+
    "\2\24\1\0\32\24\1\25\1\11\1\24\1\25\27\24"+
    "\1\0\2\11\1\0\1\26\30\0\2\11\1\0\1\27"+
    "\54\0\1\31\1\32\4\0\1\32\23\0\1\52\4\0"+
    "\1\53\1\52\25\0\1\52\1\0\3\32\1\53\1\52"+
    "\1\0\1\32\1\0\2\11\1\0\1\33\54\0\3\35"+
    "\3\0\1\35\1\0\2\11\1\0\1\36\54\0\3\37"+
    "\3\0\1\37\1\0\2\11\1\0\1\40\41\0\1\54"+
    "\21\0\2\43\1\0\32\43\1\44\1\11\1\43\1\44"+
    "\27\43\1\0\2\11\1\0\1\45\42\0\1\55\43\0"+
    "\1\56\34\0\1\57\3\60\2\0\1\57\1\60\25\0"+
    "\3\61\3\0\1\61\12\0\1\42\34\0\1\62\37\0"+
    "\1\63\41\0\3\60\3\0\1\60\23\0\1\52\1\0"+
    "\3\61\1\0\1\52\1\0\1\61\20\0\1\64\23\0"+
    "\1\65\44\0\1\66\25\0\1\67\20\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1176];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\7\0\1\11\2\1\2\11\1\1\4\11\12\1\1\11"+
    "\5\1\1\11\3\1\2\11\10\0\2\1\4\0\2\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[55];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  ObjLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        zzDoEOF();
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return TokenType.BAD_CHARACTER;
            } 
            // fall through
          case 28: break;
          case 2: 
            { yybegin(YYINITIAL); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 29: break;
          case 3: 
            { yybegin(YYINITIAL); return ObjTypes.COMMENT;
            } 
            // fall through
          case 30: break;
          case 4: 
            { yybegin(WAITING_STRING); return ObjTypes.OBJECT_KEYWORD;
            } 
            // fall through
          case 31: break;
          case 5: 
            { yybegin(WAITING_STRING); return ObjTypes.GROUP_KEYWORD;
            } 
            // fall through
          case 32: break;
          case 6: 
            { yybegin(WAITING_FLOAT); return ObjTypes.VERTEX_KEYWORD;
            } 
            // fall through
          case 33: break;
          case 7: 
            { yybegin(WAITING_FACE_VERTEX); return ObjTypes.FACE_KEYWORD;
            } 
            // fall through
          case 34: break;
          case 8: 
            { yybegin(WAITING_VERTEX_INDEX); return ObjTypes.LINE_KEYWORD;
            } 
            // fall through
          case 35: break;
          case 9: 
            { yybegin(WAITING_VERTEX_INDEX); return ObjTypes.POINT_KEYWORD;
            } 
            // fall through
          case 36: break;
          case 10: 
            { yybegin(WAITING_FLAG); return ObjTypes.SMOOTH_SHADING_KEYWORD;
            } 
            // fall through
          case 37: break;
          case 11: 
            { yybegin(YYINITIAL); return ObjTypes.STRING;
            } 
            // fall through
          case 38: break;
          case 12: 
            { yybegin(WAITING_STRING); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 39: break;
          case 13: 
            { yybegin(WAITING_FLOAT); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 40: break;
          case 14: 
            { yybegin(WAITING_FLOAT); return ObjTypes.FLOAT;
            } 
            // fall through
          case 41: break;
          case 15: 
            { yybegin(WAITING_FACE_VERTEX); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 42: break;
          case 16: 
            { yybegin(WAITING_FACE_VERTEX); return ObjTypes.VERTEX_INDEX_SEPARATOR;
            } 
            // fall through
          case 43: break;
          case 17: 
            { yybegin(WAITING_FACE_VERTEX); return ObjTypes.INDEX;
            } 
            // fall through
          case 44: break;
          case 18: 
            { yybegin(WAITING_VERTEX_INDEX); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 45: break;
          case 19: 
            { yybegin(WAITING_VERTEX_INDEX); return ObjTypes.INDEX;
            } 
            // fall through
          case 46: break;
          case 20: 
            { yybegin(WAITING_FLAG); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 47: break;
          case 21: 
            { yybegin(YYINITIAL); return ObjTypes.FLAG;
            } 
            // fall through
          case 48: break;
          case 22: 
            { yybegin(YYINITIAL); return ObjTypes.REFERENCE;
            } 
            // fall through
          case 49: break;
          case 23: 
            { yybegin(WAITING_REFERENCE); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 50: break;
          case 24: 
            { yybegin(WAITING_FLOAT); return ObjTypes.TEXTURE_COORDINATES_KEYWORD;
            } 
            // fall through
          case 51: break;
          case 25: 
            { yybegin(WAITING_FLOAT); return ObjTypes.VERTEX_NORMAL_KEYWORD;
            } 
            // fall through
          case 52: break;
          case 26: 
            { yybegin(WAITING_REFERENCE); return ObjTypes.MATERIAL_FILE_REF_KEYWORD;
            } 
            // fall through
          case 53: break;
          case 27: 
            { yybegin(WAITING_REFERENCE); return ObjTypes.MATERIAL_REFERENCE_KEYWORD;
            } 
            // fall through
          case 54: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
