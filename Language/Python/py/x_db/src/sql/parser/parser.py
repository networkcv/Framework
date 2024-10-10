import re

import sly

from .ast import *


class SQLLexer(sly.Lexer):
    CREATE = 'CREATE'
    DROP = 'DROP'
    DATABASE = 'DATABASE'
    TABLE = 'TABLE'
    INDEX = 'INDEX'
    INSERT = 'INSERT'
    INTO = 'INTO'
    UPDATE = 'UPDATE'
    DELETE = 'DELETE'
    VALUES = 'VALUES'
    SET = 'SET'
    SELECT = 'SELECT'
    STAR = r'\*'
    FROM = 'FROM'
    WHERE = 'WHERE'
    ORDER_BY = 'ORDER_BY'
    GROUPY_BY = 'GROUPY_BY'
    JOIN = 'JOIN'
    FULL = 'FULL'
    LEFT = 'LEFT'
    RIGHT = 'RIGHT'
    INNER = 'INNER'
    OUTER = 'OUTER'
    ON = 'ON'
    DOT = r'\.'
    COMMA = r'\,'
    LPAREN = r'\('
    RPAREN = r'\)'
    EQ = r'='
    NE = r'!='
    GT = r'>'
    GEQ = r'>='
    LT = r'<'
    LEQ = r'<='
    AND = r'\bAND\b'
    OR = r'\bOR\b'
    NOT = r'\bNOT\b'
    EXPLAIN = 'EXPLAIN'
    ID = r'[a-zA-Z_][a-zA-Z0-9_]*'
    INTEGER = r'\d+'
    QUOTE_STRING = r"'[^']*'"
    DUOTE_STRING = r'"[^"]*"'
    NULL = 'NULL'

    ignore = ' \t\n\r'
    reflags = re.IGNORECASE

    token = {
        # DDL
        CREATE, DROP, DATABASE, TABLE, INDEX,
        # DML
        INSERT, INTO, UPDATE, DELETE, VALUES, SET,
        # DQL
        SELECT, STAR, FROM, WHERE, ORDER_BY, GROUPY_BY,
        # join
        JOIN, FULL, LEFT, RIGHT, INNER, OUTER, ON,
        # punctuntion
        DOT, COMMA, LPAREN, RPAREN,
        # operators
        EQ, NE, GT, GEQ, LT, LEQ, AND, OR, NOT,
        # others
        EXPLAIN,
        # data type identitfier
        ID, INTEGER, QUOTE_STRING, DUOTE_STRING, NULL,
    }


# select a,b from t1;
# select a,b from t1 where a > c;
# select a,b from t1 where a > c order by b;
# select a,count(a) from t1 where a > c group by a;
class SQLParser(sly.Parser):

    @_('')
    def select(self):
        return Select
