/*
 * Copyright 2020-2023 Slawomir Czerwinski
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

WHITE_SPACE=[\ \t]+

OBJECT_KEYWORD="o"
GROUP_KEYWORD="g"

VERTEX_KEYWORD="v"
TEXTURE_COORDINATES_KEYWORD="vt"
VERTEX_NORMAL_KEYWORD="vn"

FACE_KEYWORD="f"
LINE_KEYWORD="l"
POINT_KEYWORD="p"

VERTEX_INDEX_SEPARATOR=\/

SMOOTHING_GROUP_KEYWORD="s"
SMOOTHING_GROUP_NUMBER=([1-9][\d]*)|((0)|(off))

MATERIAL_FILE_REF_KEYWORD="mtllib"
MATERIAL_REFERENCE_KEYWORD="usemtl"

OBJECT_OR_GROUP_NAME=[^\ \t\r\n\f]+
FLOAT="-"?((0)|([1-9][\d]*))("."[\d]+)?([Ee][+-]?[\d]+)?
INDEX=-?([1-9][\d]*)
REFERENCE=[^\ \t\r\n\f]+

%state WAITING_OBJECT_OR_GROUP_NAME
%state WAITING_FLOAT
%state WAITING_FACE_VERTEX
%state WAITING_LINE_VERTEX
%state WAITING_VERTEX_INDEX
%state WAITING_SMOOTHING_GROUP_NUMBER
%state WAITING_MATERIAL_FILE_NAME
%state WAITING_MATERIAL_NAME

%state INVALID

%%

<YYINITIAL> {COMMENT_LINE} { yybegin(YYINITIAL); return ObjTypes.COMMENT; }

<YYINITIAL> {OBJECT_KEYWORD} { yybegin(WAITING_OBJECT_OR_GROUP_NAME); return ObjTypes.OBJECT_KEYWORD; }
<YYINITIAL> {GROUP_KEYWORD} { yybegin(WAITING_OBJECT_OR_GROUP_NAME); return ObjTypes.GROUP_KEYWORD; }

<YYINITIAL> {VERTEX_KEYWORD} { yybegin(WAITING_FLOAT); return ObjTypes.VERTEX_KEYWORD; }
<YYINITIAL> {TEXTURE_COORDINATES_KEYWORD} { yybegin(WAITING_FLOAT); return ObjTypes.TEXTURE_COORDINATES_KEYWORD; }
<YYINITIAL> {VERTEX_NORMAL_KEYWORD} { yybegin(WAITING_FLOAT); return ObjTypes.VERTEX_NORMAL_KEYWORD; }

<YYINITIAL> {FACE_KEYWORD} { yybegin(WAITING_FACE_VERTEX); return ObjTypes.FACE_KEYWORD; }
<YYINITIAL> {LINE_KEYWORD} { yybegin(WAITING_LINE_VERTEX); return ObjTypes.LINE_KEYWORD; }
<YYINITIAL> {POINT_KEYWORD} { yybegin(WAITING_VERTEX_INDEX); return ObjTypes.POINT_KEYWORD; }

<YYINITIAL> {SMOOTHING_GROUP_KEYWORD} { yybegin(WAITING_SMOOTHING_GROUP_NUMBER); return ObjTypes.SMOOTHING_GROUP_KEYWORD; }

<YYINITIAL> {MATERIAL_FILE_REF_KEYWORD} { yybegin(WAITING_MATERIAL_FILE_NAME); return ObjTypes.MATERIAL_FILE_REF_KEYWORD; }
<YYINITIAL> {MATERIAL_REFERENCE_KEYWORD} { yybegin(WAITING_MATERIAL_NAME); return ObjTypes.MATERIAL_REFERENCE_KEYWORD; }

<WAITING_OBJECT_OR_GROUP_NAME> {WHITE_SPACE} { yybegin(WAITING_OBJECT_OR_GROUP_NAME); return TokenType.WHITE_SPACE; }
<WAITING_OBJECT_OR_GROUP_NAME> {OBJECT_OR_GROUP_NAME} { yybegin(YYINITIAL); return ObjTypes.OBJECT_OR_GROUP_NAME; }

<WAITING_FLOAT> {WHITE_SPACE} { yybegin(WAITING_FLOAT); return TokenType.WHITE_SPACE; }
<WAITING_FLOAT> {FLOAT} { yybegin(WAITING_FLOAT); return ObjTypes.FLOAT; }

<WAITING_FACE_VERTEX> {WHITE_SPACE} { yybegin(WAITING_FACE_VERTEX); return TokenType.WHITE_SPACE; }
<WAITING_FACE_VERTEX> {VERTEX_INDEX_SEPARATOR} { yybegin(WAITING_FACE_VERTEX); return ObjTypes.VERTEX_INDEX_SEPARATOR; }
<WAITING_FACE_VERTEX> {INDEX} { yybegin(WAITING_FACE_VERTEX); return ObjTypes.INDEX; }

<WAITING_LINE_VERTEX> {WHITE_SPACE} { yybegin(WAITING_LINE_VERTEX); return TokenType.WHITE_SPACE; }
<WAITING_LINE_VERTEX> {VERTEX_INDEX_SEPARATOR} { yybegin(WAITING_LINE_VERTEX); return ObjTypes.VERTEX_INDEX_SEPARATOR; }
<WAITING_LINE_VERTEX> {INDEX} { yybegin(WAITING_LINE_VERTEX); return ObjTypes.INDEX; }

<WAITING_VERTEX_INDEX> {WHITE_SPACE} { yybegin(WAITING_VERTEX_INDEX); return TokenType.WHITE_SPACE; }
<WAITING_VERTEX_INDEX> {INDEX} { yybegin(WAITING_VERTEX_INDEX); return ObjTypes.INDEX; }

<WAITING_SMOOTHING_GROUP_NUMBER> {WHITE_SPACE} { yybegin(WAITING_SMOOTHING_GROUP_NUMBER); return TokenType.WHITE_SPACE; }
<WAITING_SMOOTHING_GROUP_NUMBER> {SMOOTHING_GROUP_NUMBER} { yybegin(YYINITIAL); return ObjTypes.SMOOTHING_GROUP_NUMBER; }

<WAITING_MATERIAL_FILE_NAME> {WHITE_SPACE} { yybegin(WAITING_MATERIAL_FILE_NAME); return TokenType.WHITE_SPACE; }
<WAITING_MATERIAL_FILE_NAME> {REFERENCE} { yybegin(WAITING_MATERIAL_FILE_NAME); return ObjTypes.MATERIAL_FILE_NAME; }

<WAITING_MATERIAL_NAME> {WHITE_SPACE} { yybegin(WAITING_MATERIAL_NAME); return TokenType.WHITE_SPACE; }
<WAITING_MATERIAL_NAME> {REFERENCE} { yybegin(YYINITIAL); return ObjTypes.MATERIAL_NAME; }

({CRLF}|{WHITE_SPACE})+ { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^\ \t\r\n\f\/]+ { yybegin(INVALID); return TokenType.BAD_CHARACTER; }
{VERTEX_INDEX_SEPARATOR} { yybegin(INVALID); return TokenType.BAD_CHARACTER; }

. { yybegin(INVALID); return TokenType.BAD_CHARACTER; }
