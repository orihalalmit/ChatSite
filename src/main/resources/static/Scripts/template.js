var repeaterRegex = /([\w\d]+)[\s]+\-\>[\s]+((?:.|\s)*)/;

function get(obj, myPath) {
    if(myPath.startsWith("[")) {
        return eval("obj" + myPath);
    }
    return eval("obj." + myPath.trim());
}

function templateURLVariableReplace(templateUrl, jsonObj, complete) {
    $.get(templateUrl, function (data) {
        complete(templateVariableReplace(data, jsonObj));
    });
}



function templateVariableReplace(template, jsonObj) {
    arr = splitStringByBracketsGroups(template);
    arr = arr.map(function(element) {
        if(element.startsWith("{{")) {
            value = element.substring(2, element.length - 2);
            return handleTemplateValue(value, jsonObj);
        }
        return element;
    });
    return arr.join('');
}



function handleTemplateValue(value, jsonObj) {
    if(repeaterRegex.test(value)) {
        var ma = repeaterRegex.exec(value);
        var obj = jsonObj;
        if(!ma[1].trim().startsWith("self")) {
            obj = jsonObj[ma[1].trim()];
        }
        var inner_template = ma[2].trim();
        if(Array.isArray(obj)) {
            str = "";
            obj.forEach((value, i) => {
                str += "\n" + templateVariableReplace(inner_template, { "_v": value, "_i": i });
            });
            return str;
        }
    }
    return get(jsonObj, value);
}

function splitStringByBracketsGroups(str) {
    arr = [];
    while((index = str.indexOf("{{")) != -1) {
        arr.push(str.substring(0, index));
        str = str.substring(index);
        endIndex = getBracketsEndIndex(str);
        arr.push(str.substring(0, endIndex));
        str = str.substring(endIndex);
    }
    arr.push(str);
    return arr;
}

function getBracketsEndIndex(str, startIndex = 0) {
    var openCounter = 0;
    var closeCounter = 0;
    var index = startIndex;
    do {
        if(str[index] == "{")
            openCounter++;
        else if(str[index] == "}")
            closeCounter++;
        index++;
    } while(openCounter != closeCounter);
    return index;

}





