json.array!(@users) do |user|
  json.extract! user, :id, :username, :first_name, :last_name, :password, :email, :type, :faculty, :university, :department, :year
  json.url user_url(user, format: :json)
end
