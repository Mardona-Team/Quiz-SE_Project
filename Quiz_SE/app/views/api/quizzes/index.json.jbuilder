json.array!(@quizzes) do |quiz|
  json.extract! quiz, :id, :title, :subject, :year, :description, :marks, :status
  json.url quiz_url(quiz, format: :json)
end
