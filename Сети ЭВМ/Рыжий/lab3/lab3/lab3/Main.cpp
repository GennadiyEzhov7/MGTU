#define  _CRT_SECURE_NO_WARNINGS 1

#include <iostream>
#include <map>
#include <regex>
#include <random>
#include <math.h>
#include <fstream>

using namespace std;

const int DEF = -99999999;
ofstream debug;

string encodeVal(int val) {
    return "salt_" + to_string(val);
}

string decodeVal(string val) {
    if (val.size() > 5)
        return val.substr(5);
    
    return val;
}

bool isInteger(const std::string& s) {
    return std::regex_match(s, std::regex("^([-+]?)[0-9]{1,6}$"));
}

bool valIsValid(string name, map<string, string>* queryData, string* message) {
    string val = (*queryData)[name];
    string parsedVal = decodeVal(val);
    if (isInteger(parsedVal)) {
        (*queryData)[name] = parsedVal;
        return true;
    }
    else {
        *message = string("Invalid request param! Dont try to hack the game!");
        return false;
    }
}

map<string, string>* parse(const string& query)
{
    map<string, string>* data = new map<string, string>;
    regex pattern("([\\w+%]+)=([^&]*)");
    auto words_begin = sregex_iterator(query.begin(), query.end(), pattern);
    auto words_end = sregex_iterator();
    for (sregex_iterator i = words_begin; i != words_end; i++)
    {
        string key = (*i)[1].str();
        string value = (*i)[2].str();
        (*data)[key] = value;
    }
    return data;
}

bool mapIsValid(map<string, string>* map, string* message) {
    return map->find("number") != map->end() && isInteger((*map)["number"])
        && map->find("hnumber") != map->end() && valIsValid("hnumber", map, message)
        && map->find("attemptCount") != map->end() && valIsValid("attemptCount", map, message)
        && map->find("rangeMin") != map->end() && valIsValid("rangeMin", map, message)
        && map->find("rangeMax") != map->end() && valIsValid("rangeMax", map, message);
}

bool isNewWithUserRange(map<string, string> map) {
    return isNew(map)
        && map.find("rangeMin") != map.end() && isInteger(map["rangeMin"])
        && map.find("rangeMax") != map.end() && isInteger(map["rangeMax"]);
}

bool isNew(map<string, string> map) {
    return map.find("newGame") != map.end() && map["newGame"]._Equal("on");
       
}

bool validRange(int rangeMin, int rangeMax, string* message) {
    if (rangeMax < 0 || rangeMin < 0) {
        *message = string("Range values must be >= 0");
        return false;
    }

    if (rangeMax > 999999 || rangeMin > 999999) {
        *message = string("Range difference must be <= 999999");
        return false;
    }

    if (rangeMax - rangeMin < 100) {
        *message = string("Range difference must be >= 100");
        return false;
    }

    return true;
}

int main()
{
    debug.open("C:\\Users\\mvarlamov\\Desktop\\debug.txt");
    srand(time(NULL));
    char* query = getenv("QUERY_STRING");
    map<string, string>* queryData = parse(query);
    int attemptCount = 0;
    int hNumber = 0;
    int number = DEF;
    int rangeMin = 0;
    int rangeMax = 1000;
    bool newGame = false;
    bool win = false;
    bool lose = false;
    bool error = false;
    bool wrongNumber = false;
    string* message = new string("");

    //debug << (*queryData)["hnumber"] << endl;
    //debug << valIsValid("hnumber", queryData, message) << endl;
    //debug << (*queryData)["hnumber"];


    if (queryData->size() == 0) {
        hNumber = rand() % rangeMax + rangeMin;
        attemptCount = log2(rangeMax - rangeMin + 1);
    }
    else if (isNewWithUserRange((*queryData))) {
        rangeMin = stoi((*queryData)["rangeMin"]);
        rangeMax = stoi((*queryData)["rangeMax"]);
        error = !validRange(rangeMin, rangeMax, message);
        if (!error) {
            hNumber = rand() % rangeMax + rangeMin;
            attemptCount = log2(rangeMax - rangeMin + 1);
        }
    }
    else if (isNew((*queryData))) {
        message = new string("Enter range");
        newGame = true;
    }
    else if (mapIsValid(queryData, message)) {
        hNumber = stoi((*queryData)["hnumber"]);
        attemptCount = stoi((*queryData)["attemptCount"]);
        rangeMin = stoi((*queryData)["rangeMin"]);
        rangeMax = stoi((*queryData)["rangeMax"]);
        error = !validRange(rangeMin, rangeMax, message);
        if (!error) {
            number = stoi((*queryData)["number"]);
            if (number < 0) {
                wrongNumber = true;
                message = new string("Number must be positive");
            }
            else if (number < rangeMin || number > rangeMax) {
                wrongNumber = true;
                message = new string("Number is out of range");
            }
        }
    }
    else {
        error = true;
        if (message == NULL)
            message = new string("Invalid data!");
    }

    if (!error && !newGame && !wrongNumber) {
        if (number == hNumber) {
            win = true;
            message = new string("You win!");
        }
        else if (--attemptCount <= 0) {
            lose = true;
            message = new string("You lose(");
        }

        if (number == DEF)
            message = new string("Enter your number");
        else if (number > hNumber)
            message = new string("Number is smaller than");
        else if (number < hNumber)
            message = new string("Number is bigger than");
    }

    cout << "Content-type: text/html";

    cout << endl << endl;

    cout
        << "<!DOCTYPE html>"
        << "<html>"
        << "<head>"
        << "<meta charset = \"UTF-8\">"
        << "<link rel=\"stylesheet\" href=\"/resources/bootstrap/css/bootstrap.min.css\">"
        << "<link rel=\"stylesheet\" href=\"/resources/style.css\">"
        << "</head>"
        << "<body>";

    cout
        << "<div class=\"mainContainer\">"

        << "<div class=\"centerDiv\">"
        << "<h2>Guess the number!</h2>"
        << "</div>"

        << "<form action = \"lab.cgi\">"

        << "<div class = \"input-group mb-3\" >"
        << "<span class = \"input-group-text\" id=\"basic-addon1\">" << *message << "</span>"
        //TODO мб placeholder привязать к состоянию
        << "<input class=\"form-control\" type=\"number\" required " << ((lose || win || error || newGame) ? " disabled" : "") << " name = \"number\" value=\"" << ((*queryData)["number"].empty() ? "" : (*queryData)["number"]) << "\">"
        << "</div>"

        << "<input type=\"submit\" class=\"guessButton\" value=\"Try to guess" << (newGame ? "" : " (" + to_string(attemptCount) + ")") << "\"" << ((lose || win || error || newGame) ? " disabled" : "") << "></p>"
        << "<input type=\"hidden\" name =\"hnumber\" value=\"" << encodeVal(hNumber) << "\"</p>"
        << "<input type=\"hidden\" name =\"attemptCount\" value=\"" << encodeVal(attemptCount) << "\"</p>"
        << "<input type=\"hidden\" name =\"rangeMin\" value=\"" << encodeVal(rangeMin) << "\"</p>"
        << "<input type=\"hidden\" name =\"rangeMax\" value=\"" << encodeVal(rangeMax) << "\"</p>"
        << "</form>";

    cout
        << "<form action=\"lab.cgi\">"
        << "<div class=\"centerDiv\">"
        << "<div style=\"margin: 10px auto;\"> <h4>Choose range</h4> <div class=\"symDiv\">[</div>"
        << "<input placeholder=\"0\" class=\"rangeInput\" type=\"number\" name=\"rangeMin\" value=\"" << rangeMin << "\"" << (!(lose || win || error || newGame) ? " disabled" : "") << ">"
        << " <div class=\"symDiv\">:</div> "
        << "<input placeholder=\"999999\" class=\"rangeInput\" type=\"number\" name=\"rangeMax\" value=\"" << rangeMax << "\"" << (!(lose || win || error || newGame) ? " disabled" : "") << ">"
        << "<div class=\"symDiv\"> ]</div></div>"
        << "</div>"
        << "<input class=\"restartButton\" type=\"submit\" value=\"" << ((lose || win || error || newGame) ? "Start" : "Restart") << "\">"
        << "<input type=\"radio\" name=\"newGame\"checked style=\"display: none;\">"
        << "</form>";

    cout
        << "<div class=\"centerDiv\">"
        << "<div style=\"margin: 0 auto;\">"
        << "<h2>Rules</h2> <p>"
        << "In this game you have to guess a number from " << rangeMin << " to " << rangeMax << ". Good luck!"
        << "</div>"
        << "</div>";

    cout
        << "</div>"
        << "<script src=\"/resources/bootstrap/js/bootstrap.min.js\">"
        << "</body>"
        << "</html>";
    debug.close();
}