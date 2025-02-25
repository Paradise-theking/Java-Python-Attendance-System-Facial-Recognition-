import sys
import mysql.connector
import cv2
import numpy as np
import face_recognition
from datetime import datetime, timedelta, time

# Dictionary to keep track of the last attendance time for each student
last_attendance_time = {}

# Set to store printed messages to avoid repetition
printed_messages = set()

# Create a global database connection
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="attendancedb"
)

def log_message_once(message):
    if message not in printed_messages:
        print(message)
        sys.stdout.flush()  # Flush output
        printed_messages.add(message)

def fetch_course_data():
    cursor = None
    try:
        cursor = db.cursor()
        now = datetime.now()
        current_time = now.time()
        day = now.weekday()  # Monday is 0 and Sunday is 6

        # Fetch all necessary details in one query
        sql = """
        SELECT course_code, start_time, end_time, venue FROM timetable 
        WHERE day = %s AND start_time <= %s AND end_time >= %s
        """
        cursor.execute(sql, (day, current_time, current_time))
        courses = cursor.fetchall()

        # Build a dictionary of course_code to all course details
        course_dict = {course[0]: course for course in courses}
        print(f"Course data fetched: {course_dict}")
        sys.stdout.flush()
        return course_dict

    except mysql.connector.Error as err:
        print(f"Error: {err}")
        sys.stdout.flush()
        return {}

    finally:
        if cursor is not None:
            cursor.fetchall()
            cursor.close()

def is_student_enrolled(student_no, course_code):
    cursor = None
    try:
        cursor = db.cursor()
        sql = "SELECT 1 FROM course_reg WHERE student_no = %s AND course_code = %s"
        cursor.execute(sql, (student_no, course_code))
        result = cursor.fetchone()
        return result is not None

    except mysql.connector.Error as err:
        print(f"Error: {err}")
        sys.stdout.flush()
        return False

    finally:
        if cursor is not None:
            cursor.fetchall()  # Ensure all results are fetched before closing the cursor
            cursor.close()

def markAttendance(student_no, record_type, course_code, status):
    cursor = None
    try:
        cursor = db.cursor()

        now = datetime.now()
        dtString = now.strftime('%Y-%m-%d %H:%M:%S')

        # Set status to "Clocked Out" if the record type is "ClockOut"
        if record_type == "ClockOut":
            status = "Clocked Out"

        # Insert into the MySQL database
        sql = "INSERT INTO attendance (date, time, type, student_no, course_code, status) VALUES (%s, %s, %s, %s, %s, %s)"
        val = (now.date(), dtString, record_type, student_no, course_code, status)
        cursor.execute(sql, val)
        db.commit()

        print(f"Attendance {record_type} marked for {student_no} at {dtString} for course_code {course_code} with status {status}")
        sys.stdout.flush()  # Flush output

        # Update the last attendance time
        last_attendance_time[student_no] = (now, record_type)

    except mysql.connector.Error as err:
        print(f"Error: {err}")
        sys.stdout.flush()  # Flush output

    finally:
        if cursor is not None:
            cursor.fetchall()  # Ensure all results are fetched before closing the cursor
            cursor.close()

def markAbsent(student_no, course_code):
    cursor = None
    try:
        cursor = db.cursor()

        now = datetime.now()
        dtString = now.strftime('%Y-%m-%d %H:%M:%S')
        
        # Specify the status as "Absent"
        status = "Absent"
        record_type = "Absent"

        # Insert into the MySQL database with status as "Absent"
        sql = "INSERT INTO attendance (date, student_no, course_code, status) VALUES (%s, %s, %s, %s)"
        val = (now.date(), student_no, course_code, status)
        cursor.execute(sql, val)
        db.commit()

        print(f"Absence recorded for {student_no} at {now.date()} for course_code {course_code} with status {status}")
        sys.stdout.flush()

    except mysql.connector.Error as err:
        print(f"Error: {err}")
        sys.stdout.flush()

    finally:
        if cursor is not None:
            cursor.close()

def findEncodings(images):
    encodeList = []
    for img in images:
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        encode = face_recognition.face_encodings(img)[0]
        encodeList.append(encode)
    return encodeList

def load_images_from_mysql():
    images = []
    student_numbers = []
    cursor = None
    try:
        cursor = db.cursor()

        # Query to select images from MySQL table
        sql = "SELECT student_no, image FROM images"
        cursor.execute(sql)

        for (student_no, image_blob) in cursor.fetchall():
            # Convert BLOB data to image array usable by OpenCV
            image_array = np.frombuffer(image_blob, dtype=np.uint8)
            img = cv2.imdecode(image_array, cv2.IMREAD_COLOR)
            if img is not None:
                images.append(img)
                student_numbers.append(student_no)

    except mysql.connector.Error as err:
        print(f"Error: {err}")
        sys.stdout.flush()  # Flush output

    finally:
        if cursor is not None:
            cursor.fetchall()  # Ensure all results are fetched before closing the cursor
            cursor.close()

    return images, student_numbers

# Load images and student numbers from MySQL
images, student_numbers = load_images_from_mysql()

if not images:
    print("No images found in the database.")
    sys.stdout.flush()  # Flush output
else:
    encodeListKnown = findEncodings(images)
    print('Encoding Complete')
    sys.stdout.flush()  # Flush output

    # Fetch the current courses once before starting the loop
    current_course_data = fetch_course_data()

    cap = cv2.VideoCapture(0)

    while True:
        success, img = cap.read()
        if not success:
            print("Failed to capture image")
            sys.stdout.flush()  # Flush output
            break

        imgS = cv2.resize(img, (0, 0), None, 0.25, 0.25)
        imgS = cv2.cvtColor(imgS, cv2.COLOR_BGR2RGB)

        facesCurFrame = face_recognition.face_locations(imgS)
        encodesCurFrame = face_recognition.face_encodings(imgS, facesCurFrame)

        for encodeFace, faceLoc in zip(encodesCurFrame, facesCurFrame):
            matches = face_recognition.compare_faces(encodeListKnown, encodeFace)
            faceDis = face_recognition.face_distance(encodeListKnown, encodeFace)
            matchIndex = np.argmin(faceDis)

            if matches[matchIndex]:
                student_no = student_numbers[matchIndex]
                now = datetime.now()

                # Determine if clocking in or clocking out
                if student_no in last_attendance_time:
                    last_time, last_type = last_attendance_time[student_no]
                    if last_type == "ClockIn":
                        record_type = "ClockOut"
                    else:
                        record_type = "ClockIn"
                else:
                    record_type = "ClockIn"

                # Check if the student's last attendance was recorded more than 5 minutes ago
                if student_no not in last_attendance_time or now - last_attendance_time[student_no][0] > timedelta(minutes=1):
                    # Corrected part of the code within the while loop

                    for course_code, course_info in current_course_data.items():
                        start_time_delta, end_time_delta, venue = course_info[1], course_info[2], course_info[3]

                        print(f"Processing course: {course_code}, start_time: {start_time_delta}, end_time: {end_time_delta}, venue: {venue}")
                        sys.stdout.flush()  # Debugging output

                        # Convert start_time_delta and end_time_delta to time objects for comparison
                        start_time = (datetime.min + start_time_delta).time()
                        end_time = (datetime.min + end_time_delta).time()

                        # Convert start_time to a datetime object for comparison
                        class_start_time = datetime.combine(datetime.today(), start_time)

                        # Allow scanning to start up to 10 minutes before class starts
                        if (now - class_start_time).total_seconds() >= -600:  # 10 minutes in seconds
                            if now.time() <= class_start_time.time():
                                status = "On time"
                            elif now.time() <= (class_start_time + timedelta(minutes=10)).time():
                                status = "Late"
                            else:
                                status = "Too Late"

                            if is_student_enrolled(student_no, course_code):
                                markAttendance(student_no, record_type, course_code, status)
                                y1, x2, y2, x1 = faceLoc
                                y1, x2, y2, x1 = y1 * 4, x2 * 4, y2 * 4, x1 * 4
                                cv2.rectangle(img, (x1, y1), (x2, y2), (0, 255, 0), 2)
                                cv2.rectangle(img, (x1, y2 - 35), (x2, y2), (0, 255, 0), cv2.FILLED)
                                cv2.putText(img, f"{student_no} ({status})", (x1 + 6, y2 - 6), cv2.FONT_HERSHEY_COMPLEX, 0.8, (255, 255, 255), 2)
                            else:
                                print(f"Student {student_no} not enrolled in {course_code}")
                                sys.stdout.flush()  # Flush output
                                


        cv2.imshow('Webcam', img)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()
    
# Mark absent students after the class ends
for student_no in student_numbers:
    if student_no not in last_attendance_time and is_student_enrolled(student_no, course_code):
        markAbsent(student_no, course_code)

# Close the database connection when done
db.close()

