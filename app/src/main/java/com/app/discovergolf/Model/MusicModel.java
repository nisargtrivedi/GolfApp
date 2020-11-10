package com.app.discovergolf.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

@Table(name = "tblMusic")
public class MusicModel extends Entity implements Serializable {

    @TableField(name = "mid",datatype = DATATYPE_STRING)
    public String MID;
    @TableField(name = "title",datatype = DATATYPE_STRING)
    public String title;
    @TableField(name = "file_path",datatype = DATATYPE_STRING)
    public String file_path;
    public boolean click=false;

}
//
//{
//        "error_code": "0",
//        "error_message": "Music List.",
//        "data": [
//        {
//        "id": 2,
//        "title": "Second Test Music",
//        "file_size": "434kb",
//        "file_duration": "27 second",
//        "file_path": "http://solutioncode.in/admin-asset/music/1545740763-SampleAudio_0.4mb.mp3",
//        "created_at": "2018-12-25 12:26:03",
//        "updated_at": "2018-12-25 12:26:18",
//        "status": "1"
//        },
//        {
//        "id": 3,
//        "title": "Money Ball",
//        "file_size": "test",
//        "file_duration": "1 second",
//        "file_path": "http://solutioncode.in/admin-asset/music/1545840069-Moneyball.wav",
//        "created_at": "2018-12-26 04:01:09",
//        "updated_at": "2018-12-26 04:01:35",
//        "status": "1"
//        },
//        {
//        "id": 4,
//        "title": "Snake Eyes",
//        "file_size": "100",
//        "file_duration": ":05",
//        "file_path": "http://solutioncode.in/admin-asset/music/1548131464-Redneck Rolls Dice-SoundBible.com-1100715950.wav.cypd53t.partial",
//        "created_at": "2019-01-22 04:31:04",
//        "updated_at": "2019-01-22 04:31:28",
//        "status": "1"
//        }
//        ]
//        }
