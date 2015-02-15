json.array!(@quizzes) do |quiz|
  json.id quiz.published_on?(@group) ? Publication.find_by(group_id: @group.id, quiz_id: quiz.id).id : nil
  json.title quiz.title
  json.refrence_id quiz.id
  json.published quiz.published_on(@group)
end
