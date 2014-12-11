json.array!(@groups) do |group|
  json.extract! group, :id, :group_name, :title, :year, :subject, :description
  json.url group_url(group, format: :json)
end
