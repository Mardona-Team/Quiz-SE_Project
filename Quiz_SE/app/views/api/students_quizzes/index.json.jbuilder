json.array!(@students_quizzes) do |students_quiz|
  json.extract! students_quiz, :id
  json.url students_quiz_url(students_quiz, format: :json)
end
