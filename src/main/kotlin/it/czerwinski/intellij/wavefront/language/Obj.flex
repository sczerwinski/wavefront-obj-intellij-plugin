/*
 * Copyright 2020 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import it.czerwinski.intellij.wavefront.language.psi.ObjTypes;

%%

%class ObjLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R

COMMENT_LINE="#"[^\r\n\f]*

WHITE_SPACE=[\ \t]

OBJECT_KEYWORD="o"
GROUP_KEYWORD="g"

VERTEX_KEYWORD="v"
TEXTURE_COORDINATES_KEYWORD="vt"
VERTEX_NORMAL_KEYWORD="vn"

FACE_KEYWORD="f"

VERTEX_INDEX_SEPARATOR=\/

STRING=[^\ \t\r\n\f][^\r\n\f]*
FLOAT="-"?((0)|([1-9][\d]*))"."[\d]+
INDEX=([1-9][\d]*)

%state WAITING_STRING
%state WAITING_FLOAT
%state WAITING_FACE_VERTEX

%%

<YYINITIAL> {COMMENT_LINE} { yybegin(YYINITIAL); return ObjTypes.COMMENT; }

<YYINITIAL> {OBJECT_KEYWORD} { yybegin(WAITING_STRING); return ObjTypes.OBJECT_KEYWORD; }
<YYINITIAL> {GROUP_KEYWORD} { yybegin(WAITING_STRING); return ObjTypes.GROUP_KEYWORD; }

<YYINITIAL> {VERTEX_KEYWORD} { yybegin(WAITING_FLOAT); return ObjTypes.VERTEX_KEYWORD; }
<YYINITIAL> {TEXTURE_COORDINATES_KEYWORD} { yybegin(WAITING_FLOAT); return ObjTypes.TEXTURE_COORDINATES_KEYWORD; }
<YYINITIAL> {VERTEX_NORMAL_KEYWORD} { yybegin(WAITING_FLOAT); return ObjTypes.VERTEX_NORMAL_KEYWORD; }

<YYINITIAL> {FACE_KEYWORD} { yybegin(WAITING_FACE_VERTEX); return ObjTypes.FACE_KEYWORD; }

<WAITING_STRING> {WHITE_SPACE} { yybegin(WAITING_STRING); return TokenType.WHITE_SPACE; }
<WAITING_STRING> {STRING} { yybegin(YYINITIAL); return ObjTypes.STRING; }

<WAITING_FLOAT> {WHITE_SPACE} { yybegin(WAITING_FLOAT); return TokenType.WHITE_SPACE; }
<WAITING_FLOAT> {FLOAT} { yybegin(WAITING_FLOAT); return ObjTypes.FLOAT; }

<WAITING_FACE_VERTEX> {WHITE_SPACE} { yybegin(WAITING_FACE_VERTEX); return TokenType.WHITE_SPACE; }
<WAITING_FACE_VERTEX> {VERTEX_INDEX_SEPARATOR} { yybegin(WAITING_FACE_VERTEX); return ObjTypes.VERTEX_INDEX_SEPARATOR; }
<WAITING_FACE_VERTEX> {INDEX} { yybegin(WAITING_FACE_VERTEX); return ObjTypes.INDEX; }

({CRLF}|{WHITE_SPACE})+ { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

. { return TokenType.BAD_CHARACTER; }
