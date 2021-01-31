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
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>Mtl.flex</tt>
 */
class MtlLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int WAITING_MATERIAL_NAME = 2;
  public static final int WAITING_FLOAT = 4;
  public static final int WAITING_FLAG = 6;
  public static final int WAITING_ILLUMINATION_VALUE = 8;
  public static final int WAITING_SCALAR_CHANNEL = 10;
  public static final int WAITING_REFLECTION_TYPE_OPTION_NAME = 12;
  public static final int WAITING_REFLECTION_TYPE = 14;
  public static final int WAITING_INTEGER_OPTION = 16;
  public static final int WAITING_OPTION_OR_TEXTURE = 18;
  public static final int WAITING_FLOAT_OR_OPTION_OR_TEXTURE = 20;
  public static final int INVALID = 22;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6,  6,  7,  7, 
     8,  8,  9,  9, 10, 10, 11, 11
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
    "\11\0\1\4\1\2\1\1\2\2\22\0\1\4\2\0\1\3\7\0\1\44\1\0\1\32\1\42\1\0\1\37\1\45"+
    "\10\40\13\0\1\43\5\0\1\13\2\0\1\23\5\0\1\17\12\0\1\27\1\0\1\14\1\31\1\30\1"+
    "\15\1\6\1\20\1\46\1\24\1\21\1\0\1\50\1\12\1\10\1\5\1\34\1\26\1\0\1\25\1\16"+
    "\1\11\1\22\1\33\1\7\1\35\1\36\1\47\12\0\1\1\232\0\12\41\106\0\12\41\6\0\12"+
    "\41\134\0\12\41\40\0\12\41\54\0\12\41\60\0\12\41\6\0\12\41\116\0\2\1\46\0"+
    "\12\41\26\0\12\41\74\0\12\41\16\0\62\41");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\14\0\1\1\1\2\1\3\3\1\1\4\6\1\2\5"+
    "\1\6\1\7\1\1\2\10\1\11\1\1\1\12\2\13"+
    "\1\14\1\15\1\16\1\1\1\17\2\1\1\20\1\1"+
    "\2\21\2\22\1\23\1\22\1\24\1\22\2\25\2\0"+
    "\1\26\1\27\1\30\3\0\1\31\1\0\1\32\1\33"+
    "\4\0\1\34\4\0\1\22\1\35\1\36\3\22\1\37"+
    "\2\22\11\0\2\10\3\0\1\40\3\22\1\41\1\42"+
    "\2\22\2\25\3\0\1\43\2\0\1\44\1\45\3\0"+
    "\4\22\2\0\1\46\2\0\1\47\1\0\1\50\1\51"+
    "\2\0\4\22\1\52\1\53\1\54\1\55\1\56\1\0"+
    "\1\57\5\0\2\22\1\60\1\22\7\0\1\61\1\22"+
    "\1\62\1\63\6\0\1\64\1\65\2\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[177];
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
    "\0\0\0\51\0\122\0\173\0\244\0\315\0\366\0\u011f"+
    "\0\u0148\0\u0171\0\u019a\0\u01c3\0\u01ec\0\u0215\0\u023e\0\u0267"+
    "\0\u0290\0\u02b9\0\u02e2\0\u030b\0\u0334\0\u035d\0\u0386\0\u03af"+
    "\0\u03d8\0\u0401\0\u042a\0\u0453\0\u047c\0\u04a5\0\u04ce\0\u04f7"+
    "\0\u0520\0\u0549\0\u0572\0\u01ec\0\u059b\0\u05c4\0\u01ec\0\u05ed"+
    "\0\u0616\0\u063f\0\u0668\0\u0691\0\u06ba\0\u06e3\0\u01ec\0\u070c"+
    "\0\u0735\0\u075e\0\u0787\0\u07b0\0\u07d9\0\u0802\0\u082b\0\u0854"+
    "\0\u087d\0\u08a6\0\u01ec\0\u01ec\0\u01ec\0\u08cf\0\u08f8\0\u0921"+
    "\0\u01ec\0\u094a\0\u01ec\0\u01ec\0\u0973\0\u099c\0\u09c5\0\u09ee"+
    "\0\u01ec\0\u0a17\0\u0a40\0\u0a69\0\u0a92\0\u0abb\0\u0ae4\0\u0735"+
    "\0\u0b0d\0\u0b36\0\u0b5f\0\u0735\0\u0b88\0\u0bb1\0\u0bda\0\u0c03"+
    "\0\u0c2c\0\u0c55\0\u0c7e\0\u0ca7\0\u0cd0\0\u0cf9\0\u0d22\0\u0d22"+
    "\0\u0d4b\0\u0d74\0\u0d9d\0\u0dc6\0\u0735\0\u0def\0\u0e18\0\u0e41"+
    "\0\u0735\0\u0735\0\u0e6a\0\u0e93\0\u0e93\0\u0ebc\0\u0ee5\0\u0f0e"+
    "\0\u0f37\0\u01ec\0\u0f60\0\u0f89\0\u01ec\0\u01ec\0\u0fb2\0\u0fdb"+
    "\0\u1004\0\u102d\0\u1056\0\u107f\0\u10a8\0\u10d1\0\u10fa\0\u01ec"+
    "\0\u1123\0\u03d8\0\u01ec\0\u114c\0\u01ec\0\u01ec\0\u1175\0\u119e"+
    "\0\u11c7\0\u11f0\0\u1219\0\u1242\0\u01ec\0\u01ec\0\u01ec\0\u01ec"+
    "\0\u01ec\0\u126b\0\u01ec\0\u1294\0\u12bd\0\u12e6\0\u130f\0\u1338"+
    "\0\u1361\0\u138a\0\u0735\0\u13b3\0\u13dc\0\u1405\0\u142e\0\u1457"+
    "\0\u1480\0\u14a9\0\u14d2\0\u0735\0\u14fb\0\u0735\0\u0735\0\u1524"+
    "\0\u154d\0\u1576\0\u159f\0\u15c8\0\u15f1\0\u0735\0\u01ec\0\u161a"+
    "\0\u1643";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[177];
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
    "\1\15\2\16\1\17\1\16\1\20\2\15\1\21\2\15"+
    "\1\22\1\15\1\23\1\24\1\25\1\15\1\26\1\15"+
    "\1\27\1\15\1\30\3\15\1\31\17\15\1\32\1\33"+
    "\1\16\1\32\1\34\44\32\1\15\2\16\1\15\1\35"+
    "\25\15\1\36\4\15\1\37\1\40\4\15\1\40\4\15"+
    "\2\16\1\15\1\41\27\15\1\42\15\15\2\16\1\15"+
    "\1\43\32\15\2\44\4\15\1\45\4\15\2\16\1\15"+
    "\1\46\3\15\1\47\1\15\1\47\12\15\1\47\3\15"+
    "\1\47\14\15\2\47\2\15\2\16\1\15\1\50\25\15"+
    "\1\51\17\15\2\16\1\15\1\52\11\15\1\53\11\15"+
    "\1\54\21\15\2\16\1\15\1\55\25\15\1\56\4\15"+
    "\1\57\1\60\4\15\1\60\3\15\1\61\1\62\1\16"+
    "\1\61\1\63\25\61\1\64\17\61\1\62\1\16\1\61"+
    "\1\65\25\61\1\66\4\61\1\67\1\70\4\61\1\70"+
    "\3\61\1\15\2\16\1\15\1\16\44\15\52\0\2\16"+
    "\1\0\1\16\44\0\2\17\1\0\46\17\6\0\1\71"+
    "\56\0\1\72\50\0\1\73\1\74\1\75\40\0\1\76"+
    "\12\0\1\77\53\0\1\100\44\0\1\101\42\0\1\102"+
    "\54\0\1\103\2\0\1\104\35\0\1\105\64\0\1\106"+
    "\26\0\2\32\1\0\1\32\1\0\45\32\1\33\1\16"+
    "\1\32\1\16\44\32\1\0\2\16\1\0\1\34\45\0"+
    "\2\16\1\0\1\35\103\0\1\37\1\40\4\0\1\40"+
    "\11\0\1\107\33\0\1\110\1\107\13\0\1\107\30\0"+
    "\3\40\1\110\1\107\1\0\1\40\4\0\2\16\1\0"+
    "\1\41\51\0\1\111\12\0\1\112\31\0\2\16\1\0"+
    "\1\43\103\0\1\44\12\0\2\16\1\0\1\46\45\0"+
    "\2\16\1\0\1\50\55\0\1\113\40\0\2\16\1\0"+
    "\1\52\72\0\1\114\44\0\1\115\27\0\2\16\1\0"+
    "\1\55\103\0\1\57\1\60\4\0\1\60\42\0\3\60"+
    "\3\0\1\60\3\0\2\61\1\0\1\61\1\0\45\61"+
    "\1\62\1\16\1\61\1\16\44\61\1\0\2\16\1\0"+
    "\1\63\44\0\2\61\1\0\1\61\1\0\3\61\1\116"+
    "\1\117\4\61\1\120\2\61\1\121\6\61\1\122\1\123"+
    "\2\61\1\124\14\61\1\0\2\16\1\0\1\65\44\0"+
    "\2\61\1\0\1\61\1\0\3\61\1\116\1\117\4\61"+
    "\1\120\2\61\1\121\6\61\1\122\1\123\2\61\1\124"+
    "\2\61\1\67\1\70\4\61\1\70\5\61\1\0\1\61"+
    "\1\0\1\61\1\125\33\61\1\126\1\125\7\61\1\0"+
    "\1\61\1\0\1\61\1\125\30\61\3\70\1\126\1\125"+
    "\1\61\1\70\3\61\7\0\1\127\67\0\1\130\52\0"+
    "\1\131\36\0\1\132\46\0\1\133\46\0\1\134\56\0"+
    "\1\135\40\0\1\136\72\0\1\137\4\0\3\140\2\0"+
    "\1\137\1\140\42\0\3\141\3\0\1\141\23\0\1\111"+
    "\66\0\1\142\36\0\1\143\55\0\1\144\17\0\2\61"+
    "\1\0\1\61\1\0\3\61\1\145\42\61\1\0\1\61"+
    "\1\0\1\61\1\146\44\61\1\0\1\61\1\0\3\61"+
    "\1\147\42\61\1\0\1\61\1\0\5\61\1\150\15\61"+
    "\1\151\22\61\1\0\1\61\1\0\3\61\1\152\1\61"+
    "\1\153\40\61\1\0\1\61\1\0\25\61\1\154\4\61"+
    "\3\155\2\61\1\154\1\155\5\61\1\0\1\61\1\0"+
    "\32\61\3\156\3\61\1\156\3\61\10\0\1\157\67\0"+
    "\1\160\35\0\1\161\62\0\1\162\47\0\1\163\45\0"+
    "\1\164\40\0\1\165\64\0\1\166\61\0\3\140\3\0"+
    "\1\140\11\0\1\107\30\0\3\141\1\0\1\107\1\0"+
    "\1\141\31\0\1\167\30\0\1\170\50\0\1\171\42\0"+
    "\2\61\1\0\1\61\1\0\30\61\1\172\15\61\1\0"+
    "\1\61\1\0\13\61\1\173\32\61\1\0\1\61\1\0"+
    "\7\61\1\174\36\61\1\0\1\61\1\0\1\61\1\175"+
    "\44\61\1\0\1\61\1\0\32\61\3\155\3\61\1\155"+
    "\5\61\1\0\1\61\1\0\1\61\1\125\30\61\3\156"+
    "\1\61\1\125\1\61\1\156\3\61\11\0\1\176\52\0"+
    "\1\177\1\0\1\200\5\0\1\201\5\0\1\202\31\0"+
    "\1\203\64\0\1\204\32\0\1\205\46\0\1\206\67\0"+
    "\1\207\52\0\1\210\21\0\2\61\1\0\1\61\1\0"+
    "\20\61\1\211\25\61\1\0\1\61\1\0\23\61\1\212"+
    "\22\61\1\0\1\61\1\0\3\61\1\213\42\61\1\0"+
    "\1\61\1\0\1\214\43\61\12\0\1\215\52\0\1\216"+
    "\1\217\1\220\50\0\1\221\37\0\1\222\51\0\1\223"+
    "\53\0\1\224\1\225\5\0\1\226\4\0\1\227\3\0"+
    "\1\230\17\0\2\61\1\0\1\61\1\0\1\61\1\231"+
    "\44\61\1\0\1\61\1\0\17\61\1\232\26\61\1\0"+
    "\1\61\1\0\21\61\1\233\24\61\1\0\1\61\1\0"+
    "\10\61\1\234\33\61\6\0\1\235\76\0\1\236\22\0"+
    "\1\237\67\0\1\240\44\0\1\241\43\0\1\242\17\0"+
    "\1\243\14\0\2\61\1\0\1\61\1\0\11\61\1\244"+
    "\34\61\1\0\1\61\1\0\7\61\1\245\36\61\1\0"+
    "\1\61\1\0\15\61\1\246\10\61\1\247\15\61\16\0"+
    "\1\250\60\0\1\223\42\0\1\251\64\0\1\252\62\0"+
    "\1\253\32\0\1\254\31\0\1\255\37\0\2\61\1\0"+
    "\1\61\1\0\1\256\43\61\16\0\1\257\43\0\1\223"+
    "\44\0\1\251\67\0\1\251\74\0\1\223\11\0\1\260"+
    "\73\0\1\261\24\0\1\223\40\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5740];
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
    "\14\0\1\11\26\1\1\11\2\1\1\11\7\1\1\11"+
    "\11\1\2\0\3\11\3\0\1\11\1\0\2\11\4\0"+
    "\1\11\4\0\11\1\11\0\2\1\3\0\12\1\3\0"+
    "\1\11\2\0\2\11\3\0\4\1\2\0\1\11\2\0"+
    "\1\11\1\0\2\11\2\0\4\1\5\11\1\0\1\11"+
    "\5\0\4\1\7\0\4\1\6\0\1\1\1\11\2\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[177];
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
  MtlLexer(java.io.Reader in) {
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
            { yybegin(INVALID); return TokenType.BAD_CHARACTER;
            } 
            // fall through
          case 54: break;
          case 2: 
            { yybegin(YYINITIAL); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 55: break;
          case 3: 
            { yybegin(YYINITIAL); return MtlTypes.COMMENT;
            } 
            // fall through
          case 56: break;
          case 4: 
            { yybegin(WAITING_FLOAT); return MtlTypes.DISSOLVE_KEYWORD;
            } 
            // fall through
          case 57: break;
          case 5: 
            { yybegin(YYINITIAL); return MtlTypes.MATERIAL_NAME;
            } 
            // fall through
          case 58: break;
          case 6: 
            { yybegin(WAITING_MATERIAL_NAME); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 59: break;
          case 7: 
            { yybegin(WAITING_FLOAT); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 60: break;
          case 8: 
            { yybegin(WAITING_FLOAT); return MtlTypes.FLOAT;
            } 
            // fall through
          case 61: break;
          case 9: 
            { yybegin(WAITING_FLAG); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 62: break;
          case 10: 
            { yybegin(WAITING_ILLUMINATION_VALUE); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 63: break;
          case 11: 
            { yybegin(YYINITIAL); return MtlTypes.ILLUMINATION_VALUE;
            } 
            // fall through
          case 64: break;
          case 12: 
            { yybegin(WAITING_SCALAR_CHANNEL); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 65: break;
          case 13: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.SCALAR_CHANNEL;
            } 
            // fall through
          case 66: break;
          case 14: 
            { yybegin(WAITING_REFLECTION_TYPE_OPTION_NAME); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 67: break;
          case 15: 
            { yybegin(WAITING_REFLECTION_TYPE); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 68: break;
          case 16: 
            { yybegin(WAITING_INTEGER_OPTION); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 69: break;
          case 17: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.INTEGER;
            } 
            // fall through
          case 70: break;
          case 18: 
            { yybegin(YYINITIAL); return MtlTypes.TEXTURE_FILE;
            } 
            // fall through
          case 71: break;
          case 19: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 72: break;
          case 20: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 73: break;
          case 21: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return MtlTypes.FLOAT;
            } 
            // fall through
          case 74: break;
          case 22: 
            { yybegin(WAITING_FLOAT); return MtlTypes.AMBIENT_COLOR_KEYWORD;
            } 
            // fall through
          case 75: break;
          case 23: 
            { yybegin(WAITING_FLOAT); return MtlTypes.DIFFUSE_COLOR_KEYWORD;
            } 
            // fall through
          case 76: break;
          case 24: 
            { yybegin(WAITING_FLOAT); return MtlTypes.SPECULAR_COLOR_KEYWORD;
            } 
            // fall through
          case 77: break;
          case 25: 
            { yybegin(WAITING_FLOAT); return MtlTypes.TRANSMISSION_FILTER_KEYWORD;
            } 
            // fall through
          case 78: break;
          case 26: 
            { yybegin(WAITING_FLOAT); return MtlTypes.SPECULAR_EXPONENT_KEYWORD;
            } 
            // fall through
          case 79: break;
          case 27: 
            { yybegin(WAITING_FLOAT); return MtlTypes.OPTICAL_DENSITY_KEYWORD;
            } 
            // fall through
          case 80: break;
          case 28: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.FLAG;
            } 
            // fall through
          case 81: break;
          case 29: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return MtlTypes.TURBULENCE_OPTION_NAME;
            } 
            // fall through
          case 82: break;
          case 30: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return MtlTypes.SCALE_OPTION_NAME;
            } 
            // fall through
          case 83: break;
          case 31: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return MtlTypes.OFFSET_OPTION_NAME;
            } 
            // fall through
          case 84: break;
          case 32: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return MtlTypes.VALUE_MODIFIER_OPTION_NAME;
            } 
            // fall through
          case 85: break;
          case 33: 
            { yybegin(WAITING_FLAG); return MtlTypes.COLOR_CORRECTION_OPTION_NAME;
            } 
            // fall through
          case 86: break;
          case 34: 
            { yybegin(WAITING_FLOAT_OR_OPTION_OR_TEXTURE); return MtlTypes.BUMP_MULTIPLIER_OPTION_NAME;
            } 
            // fall through
          case 87: break;
          case 35: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.DISPLACEMENT_MAP_KEYWORD;
            } 
            // fall through
          case 88: break;
          case 36: 
            { yybegin(WAITING_REFLECTION_TYPE_OPTION_NAME); return MtlTypes.REFLECTION_MAP_KEYWORD;
            } 
            // fall through
          case 89: break;
          case 37: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.BUMP_MAP_KEYWORD;
            } 
            // fall through
          case 90: break;
          case 38: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.DISSOLVE_MAP_KEYWORD;
            } 
            // fall through
          case 91: break;
          case 39: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.STENCIL_DECAL_MAP_KEYWORD;
            } 
            // fall through
          case 92: break;
          case 40: 
            { yybegin(WAITING_ILLUMINATION_VALUE); return MtlTypes.ILLUMINATION_KEYWORD;
            } 
            // fall through
          case 93: break;
          case 41: 
            { yybegin(WAITING_REFLECTION_TYPE); return MtlTypes.REFLECTION_TYPE_OPTION_NAME;
            } 
            // fall through
          case 94: break;
          case 42: 
            { yybegin(WAITING_MATERIAL_NAME); return MtlTypes.NEW_MATERIAL_KEYWORD;
            } 
            // fall through
          case 95: break;
          case 43: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.AMBIENT_COLOR_MAP_KEYWORD;
            } 
            // fall through
          case 96: break;
          case 44: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.DIFFUSE_COLOR_MAP_KEYWORD;
            } 
            // fall through
          case 97: break;
          case 45: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.SPECULAR_COLOR_MAP_KEYWORD;
            } 
            // fall through
          case 98: break;
          case 46: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.SPECULAR_EXPONENT_MAP_KEYWORD;
            } 
            // fall through
          case 99: break;
          case 47: 
            { yybegin(WAITING_OPTION_OR_TEXTURE); return MtlTypes.REFLECTION_TYPE;
            } 
            // fall through
          case 100: break;
          case 48: 
            { yybegin(WAITING_FLAG); return MtlTypes.CLAMP_OPTION_NAME;
            } 
            // fall through
          case 101: break;
          case 49: 
            { yybegin(WAITING_INTEGER_OPTION); return MtlTypes.RESOLUTION_OPTION_NAME;
            } 
            // fall through
          case 102: break;
          case 50: 
            { yybegin(WAITING_FLAG); return MtlTypes.BLEND_U_OPTION_NAME;
            } 
            // fall through
          case 103: break;
          case 51: 
            { yybegin(WAITING_FLAG); return MtlTypes.BLEND_V_OPTION_NAME;
            } 
            // fall through
          case 104: break;
          case 52: 
            { yybegin(WAITING_SCALAR_CHANNEL); return MtlTypes.SCALAR_CHANNEL_OPTION_NAME;
            } 
            // fall through
          case 105: break;
          case 53: 
            { yybegin(WAITING_FLOAT); return MtlTypes.SHARPNESS_KEYWORD;
            } 
            // fall through
          case 106: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
