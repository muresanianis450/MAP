package model.ADT.ProcTable;

import model.statement.Statement;

import java.util.List;

public record ProcData(List<String> params, Statement body) {
}
