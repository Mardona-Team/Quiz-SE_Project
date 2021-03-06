module API

  class GroupsController < ApplicationController
    before_action :set_group, only: [:show, :edit, :update, :destroy]

  # GET /groups
  # GET /groups.json
  def index
    if (params[:query])
      @group = Group.find_by(group_name: params[:query])
      if @group
        render json: @group
      else
        respond_to do |format|
          format.json { render json: { errors: "No Groups found" }, status: :unprocessable_entity }
        end
      end
    elsif (params[:user_id])
      @groups = User.find(params[:user_id]).groups
      render json: @groups.limit(20).as_json(only: [:id, :title])
    else
      @groups = Group.all.where(instructor_id: params[:instructor_id])
      render json: @groups.limit(20).as_json(only: [:id, :title])
    end
  end

  # GET /groups/1
  # GET /groups/1.json
  def show
    if params[:student_id]
      render json: @group.memberships.find_by(student_id: params[:student_id]).present?
    else
      render json: @group
    end
  end

  # GET /groups/new
  def new
    @group = Group.new
  end

  # GET /groups/1/edit
  def edit
        @group=Group.find(params[:id])
        render json: @group

  end

  # POST /groups
  # POST /groups.json
  def create
    @group = Group.new(group_params)

    respond_to do |format|
      if @group.save
        format.json { render :show, id: @group.id }
      else
        format.json { render json: @group.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /groups/1
  # PATCH/PUT /groups/1.json
  def update
    respond_to do |format|
      if @group.update(group_params)
        format.json { render :show, id: @group.id }
      else
        format.json { render json: @group.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /groups/1
  # DELETE /groups/1.json
  def destroy
    @group.destroy
    respond_to do |format|
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_group
      @group = Group.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def group_params
      params.require(:group).permit(:group_name, :title, :year, :subject, :description, :instructor_id)
    end
  end
end