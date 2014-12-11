json.array!(@questions) do |question|
  json.extract! question, :id, :title, :right_answer_id
  json.url question_url(question, format: :json)
end
