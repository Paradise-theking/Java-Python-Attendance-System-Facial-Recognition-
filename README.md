# Computerised Attendance System Using Facial Recognition

A computerised university attendance management system that uses **facial recognition** to identify students and automatically record class attendance.

This project was developed as my **Bachelor of Computer Science final-year thesis** at Eswatini Medical Christian University. It combines **Java, Python, OpenCV, MySQL, and XAMPP** to provide an automated alternative to manual attendance registers.

## Project Overview

Traditional attendance methods can be time-consuming and may be vulnerable to errors or proxy attendance. This project explores how facial recognition can be integrated with university timetable and student-registration data to automate attendance recording.

The system verifies a student's identity through facial recognition and checks contextual information such as course registration, class timetable, venue, and attendance time before recording attendance.

## Key Features

- Facial recognition-based student identification
- Automated attendance recording
- Student course-registration verification
- Timetable and venue validation
- Clock-in and clock-out functionality
- Attendance status classification
- Detection and recording of absentees
- Lecturer and administrator interfaces
- Student attendance history
- Attendance reporting and monitoring

## Attendance Logic

Attendance is determined using the scheduled class starting time.

- **On Time** — student is recognised at or before the scheduled starting time
- **Late** — student is recognised within 10 minutes after the class begins
- **Too Late** — recognition occurs more than 10 minutes after the starting time
- **Absent** — no valid attendance is recorded for the student during the scheduled class

The system also validates whether the recognised student is registered for the course and whether the class corresponds with the scheduled venue and timetable.

## Technology Stack

| Component | Technology |
|---|---|
| Desktop Application | Java / Java Swing |
| Facial Recognition | Python, OpenCV |
| Database | MySQL |
| Server Environment | XAMPP |
| Database Connectivity | MySQL Connector |
| Recognition Input | Webcam |

## System Architecture

The project combines a Java-based management interface with a Python facial-recognition component and a MySQL database.

```text
                    ┌─────────────────────┐
                    │       Webcam        │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Python / OpenCV     │
                    │ Facial Recognition  │
                    └──────────┬──────────┘
                               │
                               ▼
┌─────────────────┐   ┌─────────────────────┐
│ Java Swing GUI  │◄─►│    MySQL Database   │
│ Admin/Lecturer  │   │ Student / Courses   │
└─────────────────┘   │ Timetable/Attendance│
                      └─────────────────────┘
