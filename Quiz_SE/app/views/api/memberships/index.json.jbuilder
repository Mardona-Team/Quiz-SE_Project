json.array!(@memberships) do |membership|
  json.extract! membership, :id, :status, :student_id, :group_id
  json.url membership_url(membership, format: :json)
end
