package com.huawei.l00379880.exam;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;


public class ExamApplicationTest {
    /*String examName1 = "Oracle Cloud Infrastructure Foundations";
    String key1 = "cdf";
    String path1 = "src/test/aaa.txt";
    String examName2 = "Oracle Cloud Infrastructure Foundations C";
    String key2 = "cdfc";
    String path2 = "src/test/bbb.txt";
    String examName3 = "Oracle Autonomous Database Cloud";
    String key3 = "oadc";
    String path3 = "src/test/ccc.txt";*/
    /*String examName = "03. Oracle Cloud Infrastructure Developer Associate";
    String key = "ocida";
    String path = "src/test/ddd.txt";*/
    /*String examName = "03. Oracle Cloud Infrastructure Developer Associate(c)";
    String key = "ocidac";
    String path = "src/test/eee.txt";*/
    /*String examName = "04. Oracle Cloud Infrastructure Cloud Operations";
    String key = "ocico";
    String path = "src/test/fff.txt";*/
    /*String examName = "05. Oracle Cloud Infrastructure Architect Associate";
    String key = "ociaa";
    String path = "src/test/ggg.txt";*/
    String examName = "06. Oracle Cloud Infrastructure Architect Professional";
    String key = "ociap";
    String path = "src/test/hhh.txt";

    @Test
    public void main() {
        List<List<String>> list = getSortedList();
        List<String> questionSqlList = getQuestionSqlList(list);
        List<String> questionOptionSqlList = getQuestionOptionSqlList(list);
        String examSql = getExamSql(list);

        doInsert(new ArrayList<String>() {{
            add("delete from exam where exam_name='" + examName + "'");
            add("delete from question where question_id like '" + key + "%'");
            add("delete from question_option where question_option_id like '" + key + "%'");
        }});
        //init();
        doInsert(questionSqlList);
        doInsert(questionOptionSqlList);
        doInsert(new ArrayList<String>(){{add(examSql);}});
    }

    private void init() {
        List<List<String>> l = getList2();

        // remove explanations.
        for(int i=0; i<l.size(); i++) {
            List<String> questionLineList = l.get(i);
            questionLineList.set(0, "Question #: "+(i+1)+" " + questionLineList.get(0));
            try {
                questionLineList.set(1, questionLineList.get(1).replace("ociaa"+(i+1)+"A","A"));
                questionLineList.set(2, questionLineList.get(2).replace("ociaa"+(i+1)+"A","B"));
                questionLineList.set(3, questionLineList.get(3).replace("ociaa"+(i+1)+"A","C"));
                questionLineList.set(4, questionLineList.get(4).replace("ociaa"+(i+1)+"A","D"));
                questionLineList.set(5, questionLineList.get(5).replace("ociaa"+(i+1)+"A","E"));
                questionLineList.set(6, questionLineList.get(6).replace("ociaa"+(i+1)+"A","F"));
                questionLineList.set(7, questionLineList.get(7).replace("ociaa"+(i+1)+"A","G"));
                questionLineList.set(8, questionLineList.get(8).replace("ociaa"+(i+1)+"A","H"));
                questionLineList.set(9, questionLineList.get(9).replace("ociaa"+(i+1)+"A","I"));
            } catch (Exception e){
            }
            for(int j=0; j<questionLineList.size(); j++) {
                System.out.println(questionLineList.get(j));
            }
            System.out.println();
        }
    }

    private String getExamSql(List<List<String>> list) {
        StringBuffer exam_question_ids = new StringBuffer("");
        StringBuffer exam_score_radio = new StringBuffer("");
        StringBuffer exam_score_check = new StringBuffer("");
        for(int i=0; i<list.size(); i++) {
            String questionId = key + (i + 1);
            exam_question_ids.append(questionId + "-");

            // answer ids
            String answerLine = list.get(i).get(list.get(i).size()-1).toLowerCase().replace(",", " ");
            String question_answer_options= answerLine.replace("answer: ","").replace(" ","-").replace("--","-");
            if(question_answer_options.contains("-")) {
                exam_score_check.append(questionId + "-");
            } else {
                exam_score_radio.append(questionId + "-");
            }
        }


        return "INSERT INTO `test`.`exam`(`exam_id`, `exam_name`, `exam_avatar`, `exam_description`, `exam_question_ids`, `exam_question_ids_radio`, `exam_question_ids_check`, `exam_question_ids_judge`, `exam_score`, `exam_score_radio`, `exam_score_check`, `exam_score_judge`, `exam_creator_id`, `exam_time_limit`, `exam_start_date`, `exam_end_date`, `create_time`, `update_time`) " +
                "select IFNULL(max(exam_id),0)+1, '"+examName+"', '"+examName+"', '"+examName+"', '"+exam_question_ids.substring(0,exam_question_ids.length()-1)+"', '"+exam_score_radio.substring(0,exam_score_radio.length()-1)+"', '"+exam_score_check.substring(0,exam_score_check.length()-1)+"', '', 6, 1, 1, 1, '68042014e23c4ebea7234cb9c77cee5c', 90, '2020-07-13 02:06:41', '2020-07-13 02:06:41', '2020-07-13 02:06:41', '2020-07-13 02:06:41' from exam";
    }

    private void doInsert(List<String> sqlList) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "123456");
            for(String sql : sqlList) {
                System.out.println(sql);
                PreparedStatement ps = conn.prepareStatement(sql);
                System.out.println(ps.executeUpdate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getQuestionOptionSqlList(List<List<String>> list) {
        List<String> questionOptionSqlList = new ArrayList<>();

        for(int i=0; i<list.size(); i++) {
            String id = key + (i+1);

            // option ids
            for(int j=1; j<list.get(i).size()-1; j++) {
                String question_option_id = list.get(i).get(j).split("\\.")[0];
                String question_option_content = list.get(i).get(j).replace(question_option_id+".","");
                // concat sql.
                questionOptionSqlList.add("INSERT INTO `question_option`(`question_option_id`, `question_option_content`, `question_option_description`) VALUES " +
                        "('"+question_option_id.toLowerCase()+"', '"+question_option_content+"', '');");
                //System.out.println(questionOptionSqlList.get(questionOptionSqlList.size()-1));
            }

        }
        return questionOptionSqlList;
    }

    private List<String> getQuestionSqlList(List<List<String>> list) {
        List<String> questionSqlList = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            String id = key + (i+1);
            String question_option_ids = "";
            String question_answer_option_ids = "";
            String question_type_id="";
            String question_name = "";

            // question name.
            question_name = list.get(i).get(0);

            // option ids
            for(int j=1; j<list.get(i).size()-1; j++) {
                question_option_ids += list.get(i).get(j).split("\\.")[0]+"-";
            }
            question_option_ids = question_option_ids.substring(0,question_option_ids.length()-1);

            // answer ids
            String question_answer_option_ids_copy= list.get(i).get(list.get(i).size()-1).toLowerCase().replace("answer: ","").replace(" ","-").replace(",","-");
            for(String answer : question_answer_option_ids_copy.split("-")) {
                question_answer_option_ids += id+answer.toLowerCase()+"-";
            }
            question_answer_option_ids = question_answer_option_ids.substring(0,question_answer_option_ids.length()-1);

            // question_type_id
            question_type_id = question_answer_option_ids.split("-").length>1?"2":"1";

            // concat sql.
            questionSqlList.add("INSERT INTO `question`(`question_id`, `question_name`, `question_score`, `question_creator_id`, `question_level_id`, `question_type_id`, `question_category_id`, `question_description`, `question_option_ids`, `question_answer_option_ids`, `create_time`, `update_time`) VALUES " +
                    "(\""+id+"\", \""+question_name+"\", 1, \"a1b661031adf4a8f969f1869d479fe74\", 1, \""+question_type_id+"\", 1, \"\", \""+question_option_ids.toLowerCase()+"\", \""+question_answer_option_ids.toLowerCase()+"\", \"2020-07-13 23:57:47\", \"2020-07-13 23:57:47\");");
        }
        return questionSqlList;
    }
    private List<List<String>> getSortedList() {
        List<List<String>> list = getList2();

        // remove explanations.
        for(int i=0; i<list.size(); i++) {
            List<String> questionLineList = list.get(i);
            List<String> questionLineListNew = new ArrayList<>();
            for(int j=0; j<questionLineList.size(); j++) {
                String line = questionLineList.get(j);
                if(!line.contains("Explanation:")) {
                    questionLineListNew.add(line);
                } else {
                    list.set(i, questionLineListNew);
                    break;
                }
            }
        }

        // concat a. b. c. d. multiple lines.
        for(int i=0; i<list.size(); i++) {
            List<String> questionLineList = list.get(i);
            List<Integer> keyIndexList = new ArrayList<>();
            for(int j=1; j<questionLineList.size()-1; j++) {
                String line = questionLineList.get(j);
                // if line contains a. b. c. etc., mark down the index.
                if(line.contains(key)) {
                    keyIndexList.add(j);
                }
            }
            keyIndexList.add(questionLineList.size()-1);

            List<String> newQuestionLineList = new ArrayList<>();
            newQuestionLineList.add(questionLineList.get(0));
            for(int x=0; x<keyIndexList.size()-1; x++) {
                String lineStr = "";
                for(int y = keyIndexList.get(x); y<keyIndexList.get(x+1); y++) {
                    lineStr += questionLineList.get(y)+" ";
                }
                newQuestionLineList.add(lineStr.replace("'", "\""));
            }
            newQuestionLineList.add(questionLineList.get(questionLineList.size()-1));
            list.set(i, newQuestionLineList);
        }

        for(int i=0; i<list.size(); i++) {
            List<String> questionLineList = list.get(i);
            // set question format.
            questionLineList.set(0, questionLineList.get(0).replace("\"","'"));

        }

        return list;
    }

    private List<List<String>> getList2() {
        List<List<String>> list = getList();
        for(int i=0; i<list.size(); i++) {
            List<String> questionLineList = list.get(i);

            for(int j=1; j<questionLineList.size(); j++) {
                String line = questionLineList.get(j);
                List<String> newQuestionLineList = new ArrayList<>();
                // if line contains a. b. c. etc., mark down the index.
                if(line.contains(key)) {
                    String firstLine = "";
                    for(int x=0; x<j; x++) {
                        firstLine += questionLineList.get(x)+" ";
                    }
                    newQuestionLineList.add(firstLine);
                    for(int x=j; x<questionLineList.size(); x++) {
                        newQuestionLineList.add(questionLineList.get(x));
                    }
                    list.set(i, newQuestionLineList);
                    break;
                }
            }
        }
        return list;
    }


    private List<List<String>> getList() {
        List<List<String>> list = new ArrayList<>(100);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String previousLine = reader.readLine();
            int number = 0;
            List<String> curList = new ArrayList<>(6);
            while (previousLine != null) {
                //question no
                if(previousLine.contains("#:")) {
                    number++;
                    if(curList.size()>0) {
                        list.add(curList);
                    }
                    curList = new ArrayList<>(6);
                    curList.add(previousLine.substring(previousLine.indexOf("#:")+2));
                } else {
                    if(!StringUtils.isEmpty(previousLine))
                        curList.add(previousLine);
                }
                // read current line
                String curLine = reader.readLine();
                if(curLine!=null) {
                    curLine = curLine.trim();
                    if (curLine.matches("^[A-Za-z][.].*$")) {
                        curLine = key + number + curLine.substring(0, curLine.indexOf(".")) + curLine.substring(curLine.indexOf(".")).replace("\t", " ");
                    }

                    previousLine = curLine;
                } else {
                    list.add(curList);
                    previousLine = null;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
